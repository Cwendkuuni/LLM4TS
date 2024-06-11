package org.jdom2;

import java.util.*;

public final class Verifier
{
    private static final byte[] VALCONST;
    private static final int[] LENCONST;
    private static final int CHARCNT = 65536;
    private static final byte[] CHARFLAGS;
    private static final byte MASKXMLCHARACTER = 1;
    private static final byte MASKXMLLETTER = 2;
    private static final byte MASKXMLSTARTCHAR = 4;
    private static final byte MASKXMLNAMECHAR = 8;
    private static final byte MASKXMLDIGIT = 16;
    private static final byte MASKXMLCOMBINING = 32;
    private static final byte MASKURICHAR = 64;
    private static final byte MASKXMLLETTERORDIGIT = 18;
    
    private static final byte[] buildBitFlags() {
        final byte[] ret = new byte[65536];
        int index = 0;
        for (int i = 0; i < Verifier.VALCONST.length; ++i) {
            final byte v = Verifier.VALCONST[i];
            int l = Verifier.LENCONST[i];
            while (--l >= 0) {
                ret[index++] = v;
            }
        }
        return ret;
    }
    
    private static final String checkJDOMName(final String name) {
        if (name == null) {
            return "XML names cannot be null";
        }
        if (name.length() == 0) {
            return "XML names cannot be empty";
        }
        if (0x0 == (Verifier.CHARFLAGS[name.charAt(0)] & 0x4)) {
            return "XML name '" + name + "' cannot begin with the character \"" + name.charAt(0) + "\"";
        }
        for (int i = name.length() - 1; i >= 1; --i) {
            if (0 == (byte)(Verifier.CHARFLAGS[name.charAt(i)] & 0x8)) {
                return "XML name '" + name + "' cannot contain the character \"" + name.charAt(i) + "\"";
            }
        }
        return null;
    }
    
    public static String checkElementName(final String name) {
        return checkJDOMName(name);
    }
    
    public static String checkAttributeName(final String name) {
        if ("xmlns".equals(name)) {
            return "An Attribute name may not be \"xmlns\"; use the Namespace class to manage namespaces";
        }
        return checkJDOMName(name);
    }
    
    public static String checkCharacterData(final String text) {
        if (text == null) {
            return "A null is not a legal XML value";
        }
        for (int len = text.length(), i = 0; i < len; ++i) {
            while (Verifier.CHARFLAGS[text.charAt(i)] != 0) {
                if (++i == len) {
                    return null;
                }
            }
            if (!isHighSurrogate(text.charAt(i))) {
                return String.format("0x%04x is not a legal XML character", (int)text.charAt(i));
            }
            if (++i >= len) {
                return String.format("Truncated Surrogate Pair 0x%04x????", (int)text.charAt(i - 1));
            }
            if (!isLowSurrogate(text.charAt(i))) {
                return String.format("Illegal Surrogate Pair 0x%04x%04x", (int)text.charAt(i - 1), (int)text.charAt(i));
            }
            if (!isXMLCharacter(decodeSurrogatePair(text.charAt(i - 1), text.charAt(i)))) {
                return String.format("0x%06x is not a legal XML character", decodeSurrogatePair(text.charAt(i - 1), text.charAt(i)));
            }
        }
        return null;
    }
    
    public static String checkCDATASection(final String data) {
        String reason = null;
        if ((reason = checkCharacterData(data)) != null) {
            return reason;
        }
        if (data.indexOf("]]>") != -1) {
            return "CDATA cannot internally contain a CDATA ending delimiter (]]>)";
        }
        return null;
    }
    
    public static String checkNamespacePrefix(final String prefix) {
        if (prefix == null || prefix.equals("")) {
            return null;
        }
        if (checkJDOMName(prefix) != null) {
            return checkJDOMName(prefix);
        }
        if (prefix.length() >= 3 && (prefix.charAt(0) == 'x' || prefix.charAt(0) == 'X') && (prefix.charAt(1) == 'm' || prefix.charAt(1) == 'M') && (prefix.charAt(2) == 'l' || prefix.charAt(2) == 'L')) {
            return "Namespace prefixes cannot begin with \"xml\" in any combination of case";
        }
        return null;
    }
    
    public static String checkNamespaceURI(final String uri) {
        if (uri == null || uri.equals("")) {
            return null;
        }
        final char first = uri.charAt(0);
        if (Character.isDigit(first)) {
            return "Namespace URIs cannot begin with a number";
        }
        if (first == '$') {
            return "Namespace URIs cannot begin with a dollar sign ($)";
        }
        if (first == '-') {
            return "Namespace URIs cannot begin with a hyphen (-)";
        }
        if (isXMLWhitespace(first)) {
            return "Namespace URIs cannot begin with white-space";
        }
        return null;
    }
    
    public static String checkNamespaceCollision(final Namespace namespace, final Namespace other) {
        String reason = null;
        final String p1 = namespace.getPrefix();
        final String u1 = namespace.getURI();
        final String p2 = other.getPrefix();
        final String u2 = other.getURI();
        if (p1.equals(p2) && !u1.equals(u2)) {
            reason = "The namespace prefix \"" + p1 + "\" collides";
        }
        return reason;
    }
    
    public static String checkNamespaceCollision(final Attribute attribute, final Element element) {
        return checkNamespaceCollision(attribute, element, -1);
    }
    
    public static String checkNamespaceCollision(final Attribute attribute, final Element element, final int ignoreatt) {
        final Namespace namespace = attribute.getNamespace();
        final String prefix = namespace.getPrefix();
        if ("".equals(prefix)) {
            return null;
        }
        return checkNamespaceCollision(namespace, element, ignoreatt);
    }
    
    public static String checkNamespaceCollision(final Namespace namespace, final Element element) {
        return checkNamespaceCollision(namespace, element, -1);
    }
    
    public static String checkNamespaceCollision(final Namespace namespace, final Element element, final int ignoreatt) {
        String reason = checkNamespaceCollision(namespace, element.getNamespace());
        if (reason != null) {
            return reason + " with the element namespace prefix";
        }
        if (element.hasAdditionalNamespaces()) {
            reason = checkNamespaceCollision(namespace, element.getAdditionalNamespaces());
            if (reason != null) {
                return reason;
            }
        }
        if (element.hasAttributes()) {
            reason = checkNamespaceCollision(namespace, element.getAttributes(), ignoreatt);
            if (reason != null) {
                return reason;
            }
        }
        return null;
    }
    
    public static String checkNamespaceCollision(final Namespace namespace, final Attribute attribute) {
        String reason = null;
        if (!attribute.getNamespace().equals(Namespace.NO_NAMESPACE)) {
            reason = checkNamespaceCollision(namespace, attribute.getNamespace());
            if (reason != null) {
                reason += " with an attribute namespace prefix on the element";
            }
        }
        return reason;
    }
    
    public static String checkNamespaceCollision(final Namespace namespace, final List<?> list) {
        return checkNamespaceCollision(namespace, list, -1);
    }
    
    public static String checkNamespaceCollision(final Namespace namespace, final List<?> list, final int ignoreatt) {
        if (list == null) {
            return null;
        }
        String reason = null;
        final Iterator<?> i = list.iterator();
        int cnt = -1;
        while (reason == null && i.hasNext()) {
            final Object obj = i.next();
            ++cnt;
            if (obj instanceof Attribute) {
                if (cnt == ignoreatt) {
                    continue;
                }
                reason = checkNamespaceCollision(namespace, (Attribute)obj);
            }
            else if (obj instanceof Element) {
                reason = checkNamespaceCollision(namespace, (Element)obj);
            }
            else {
                if (!(obj instanceof Namespace)) {
                    continue;
                }
                reason = checkNamespaceCollision(namespace, (Namespace)obj);
                if (reason == null) {
                    continue;
                }
                reason += " with an additional namespace declared by the element";
            }
        }
        return reason;
    }
    
    public static String checkProcessingInstructionTarget(final String target) {
        final String reason;
        if ((reason = checkXMLName(target)) != null) {
            return reason;
        }
        if (target.indexOf(":") != -1) {
            return "Processing instruction targets cannot contain colons";
        }
        if (target.equalsIgnoreCase("xml")) {
            return "Processing instructions cannot have a target of \"xml\" in any combination of case. (Note that the \"<?xml ... ?>\" declaration at the beginning of a document is not a processing instruction and should not be added as one; it is written automatically during output, e.g. by XMLOutputter.)";
        }
        return null;
    }
    
    public static String checkProcessingInstructionData(final String data) {
        final String reason = checkCharacterData(data);
        if (reason == null && data.indexOf("?>") >= 0) {
            return "Processing instructions cannot contain the string \"?>\"";
        }
        return reason;
    }
    
    public static String checkCommentData(final String data) {
        String reason = null;
        if ((reason = checkCharacterData(data)) != null) {
            return reason;
        }
        if (data.indexOf("--") != -1) {
            return "Comments cannot contain double hyphens (--)";
        }
        if (data.endsWith("-")) {
            return "Comment data cannot end with a hyphen.";
        }
        return null;
    }
    
    public static int decodeSurrogatePair(final char high, final char low) {
        return 65536 + (high - '\ud800') * '\u0400' + (low - '\udc00');
    }
    
    public static boolean isXMLPublicIDCharacter(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= '?' && c <= 'Z') || (c >= '\'' && c <= ';') || c == ' ' || c == '!' || c == '=' || c == '#' || c == '$' || c == '_' || c == '%' || c == '\n' || c == '\r' || c == '\t';
    }
    
    public static String checkPublicID(final String publicID) {
        String reason = null;
        if (publicID == null) {
            return null;
        }
        for (int i = 0; i < publicID.length(); ++i) {
            final char c = publicID.charAt(i);
            if (!isXMLPublicIDCharacter(c)) {
                reason = c + " is not a legal character in public IDs";
                break;
            }
        }
        return reason;
    }
    
    public static String checkSystemLiteral(final String systemLiteral) {
        String reason = null;
        if (systemLiteral == null) {
            return null;
        }
        if (systemLiteral.indexOf(39) != -1 && systemLiteral.indexOf(34) != -1) {
            reason = "System literals cannot simultaneously contain both single and double quotes.";
        }
        else {
            reason = checkCharacterData(systemLiteral);
        }
        return reason;
    }
    
    public static String checkXMLName(final String name) {
        if (name == null) {
            return "XML names cannot be null";
        }
        final int len = name.length();
        if (len == 0) {
            return "XML names cannot be empty";
        }
        if (!isXMLNameStartCharacter(name.charAt(0))) {
            return "XML names cannot begin with the character \"" + name.charAt(0) + "\"";
        }
        for (int i = 1; i < len; ++i) {
            if (!isXMLNameCharacter(name.charAt(i))) {
                return "XML names cannot contain the character \"" + name.charAt(i) + "\"";
            }
        }
        return null;
    }
    
    public static String checkURI(final String uri) {
        if (uri == null || uri.equals("")) {
            return null;
        }
        for (int i = 0; i < uri.length(); ++i) {
            final char test = uri.charAt(i);
            if (!isURICharacter(test)) {
                String msgNumber = "0x" + Integer.toHexString(test);
                if (test <= '\t') {
                    msgNumber = "0x0" + Integer.toHexString(test);
                }
                return "URIs cannot contain " + msgNumber;
            }
            if (test == '%') {
                try {
                    final char firstDigit = uri.charAt(i + 1);
                    final char secondDigit = uri.charAt(i + 2);
                    if (!isHexDigit(firstDigit) || !isHexDigit(secondDigit)) {
                        return "Percent signs in URIs must be followed by exactly two hexadecimal digits.";
                    }
                }
                catch (StringIndexOutOfBoundsException e) {
                    return "Percent signs in URIs must be followed by exactly two hexadecimal digits.";
                }
            }
        }
        return null;
    }
    
    public static boolean isHexDigit(final char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }
    
    public static boolean isHighSurrogate(final char ch) {
        return '6' == ch >>> 10;
    }
    
    public static boolean isLowSurrogate(final char ch) {
        return '7' == ch >>> 10;
    }
    
    public static boolean isURICharacter(final char c) {
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x40);
    }
    
    public static boolean isXMLCharacter(final int c) {
        if (c >= 65536) {
            return c <= 1114111;
        }
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x1);
    }
    
    public static boolean isXMLNameCharacter(final char c) {
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x8) || c == ':';
    }
    
    public static boolean isXMLNameStartCharacter(final char c) {
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x4) || c == ':';
    }
    
    public static boolean isXMLLetterOrDigit(final char c) {
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x12);
    }
    
    public static boolean isXMLLetter(final char c) {
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x2);
    }
    
    public static boolean isXMLCombiningChar(final char c) {
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x20);
    }
    
    public static boolean isXMLExtender(final char c) {
        return c >= '¶' && (c == '·' || c == '\u02d0' || c == '\u02d1' || c == '\u0387' || c == '\u0640' || c == '\u0e46' || c == '\u0ec6' || c == '\u3005' || (c >= '\u3031' && (c <= '\u3035' || (c >= '\u309d' && (c <= '\u309e' || (c >= '\u30fc' && c <= '\u30fe'))))));
    }
    
    public static boolean isXMLDigit(final char c) {
        return 0 != (byte)(Verifier.CHARFLAGS[c] & 0x10);
    }
    
    public static boolean isXMLWhitespace(final char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }
    
    public static final boolean isAllXMLWhitespace(final String value) {
        int i = value.length();
        while (--i >= 0) {
            if (!isXMLWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    static {
        VALCONST = new byte[] { 0, 1, 0, 1, 0, 1, 65, 1, 65, 73, 65, 89, 65, 1, 65, 1, 65, 79, 1, 77, 1, 79, 1, 65, 1, 9, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 9, 1, 41, 1, 41, 1, 15, 9, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 15, 1, 15, 1, 9, 15, 41, 1, 25, 1, 41, 15, 1, 15, 1, 15, 1, 15, 1, 15, 41, 15, 41, 1, 41, 1, 25, 1, 41, 1, 15, 1, 41, 15, 41, 1, 41, 1, 15, 41, 1, 25, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 15, 41, 1, 25, 15, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 15, 1, 25, 41, 15, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 15, 41, 1, 41, 1, 41, 1, 15, 1, 25, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 15, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 15, 1, 25, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 25, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 25, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 15, 1, 25, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 25, 1, 15, 1, 15, 41, 15, 41, 1, 15, 9, 41, 1, 25, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 41, 15, 41, 1, 41, 15, 1, 15, 1, 9, 1, 41, 1, 25, 1, 41, 1, 25, 1, 41, 1, 41, 1, 41, 1, 41, 15, 1, 15, 1, 41, 1, 41, 1, 41, 1, 41, 1, 41, 1, 41, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 15, 1, 41, 1, 41, 1, 15, 1, 15, 1, 15, 1, 15, 1, 9, 1, 15, 1, 15, 41, 1, 9, 1, 15, 1, 41, 1, 9, 1, 15, 1, 9, 1, 15, 1, 15, 1, 15, 1, 0, 1, 0 };
        LENCONST = new int[] { 9, 2, 2, 1, 18, 1, 1, 2, 9, 2, 1, 10, 1, 2, 1, 1, 2, 26, 4, 1, 1, 26, 3, 1, 56, 1, 8, 23, 1, 31, 1, 58, 2, 11, 2, 8, 1, 53, 1, 68, 9, 36, 3, 2, 4, 30, 56, 89, 18, 7, 14, 2, 46, 70, 26, 2, 36, 1, 1, 3, 1, 1, 1, 20, 1, 44, 1, 7, 3, 1, 1, 1, 1, 1, 1, 1, 1, 18, 13, 12, 1, 66, 1, 12, 1, 36, 1, 4, 9, 53, 2, 2, 2, 2, 3, 28, 2, 8, 2, 2, 55, 38, 2, 1, 7, 38, 10, 17, 1, 23, 1, 3, 1, 1, 1, 2, 1, 1, 11, 27, 5, 3, 46, 26, 5, 1, 10, 8, 13, 10, 6, 1, 71, 2, 5, 1, 15, 1, 4, 1, 1, 15, 2, 2, 1, 4, 2, 10, 519, 3, 1, 53, 2, 1, 1, 16, 3, 4, 3, 10, 2, 2, 10, 17, 3, 1, 8, 2, 2, 2, 22, 1, 7, 1, 1, 3, 4, 2, 1, 1, 7, 2, 2, 2, 3, 9, 1, 4, 2, 1, 3, 2, 2, 10, 2, 16, 1, 2, 6, 4, 2, 2, 22, 1, 7, 1, 2, 1, 2, 1, 2, 2, 1, 1, 5, 4, 2, 2, 3, 11, 4, 1, 1, 7, 10, 2, 3, 12, 3, 1, 7, 1, 1, 1, 3, 1, 22, 1, 7, 1, 2, 1, 5, 2, 1, 1, 8, 1, 3, 1, 3, 18, 1, 5, 10, 17, 3, 1, 8, 2, 2, 2, 22, 1, 7, 1, 2, 2, 4, 2, 1, 1, 6, 3, 2, 2, 3, 8, 2, 4, 2, 1, 3, 4, 10, 18, 2, 1, 6, 3, 3, 1, 4, 3, 2, 1, 1, 1, 2, 3, 2, 3, 3, 3, 8, 1, 3, 4, 5, 3, 3, 1, 4, 9, 1, 15, 9, 17, 3, 1, 8, 1, 3, 1, 23, 1, 10, 1, 5, 4, 7, 1, 3, 1, 4, 7, 2, 9, 2, 4, 10, 18, 2, 1, 8, 1, 3, 1, 23, 1, 10, 1, 5, 4, 7, 1, 3, 1, 4, 7, 2, 7, 1, 1, 2, 4, 10, 18, 2, 1, 8, 1, 3, 1, 23, 1, 16, 4, 6, 2, 3, 1, 4, 9, 1, 8, 2, 4, 10, 145, 46, 1, 1, 1, 2, 7, 5, 6, 1, 8, 1, 10, 39, 2, 1, 1, 2, 2, 1, 1, 2, 1, 6, 4, 1, 7, 1, 3, 1, 1, 1, 1, 2, 2, 1, 2, 1, 1, 1, 2, 6, 1, 2, 1, 2, 5, 1, 1, 1, 6, 2, 10, 62, 2, 6, 10, 11, 1, 1, 1, 1, 1, 4, 2, 8, 1, 33, 7, 20, 1, 6, 4, 6, 1, 1, 1, 21, 3, 7, 1, 1, 230, 38, 10, 39, 9, 1, 1, 2, 1, 3, 1, 1, 1, 2, 1, 5, 41, 1, 1, 1, 1, 1, 11, 1, 1, 1, 1, 1, 3, 2, 3, 1, 5, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 2, 3, 2, 1, 1, 40, 1, 9, 1, 2, 1, 2, 2, 7, 2, 1, 1, 1, 7, 40, 1, 4, 1, 8, 1, 3078, 156, 4, 90, 6, 22, 2, 6, 2, 38, 2, 6, 2, 8, 1, 1, 1, 1, 1, 1, 1, 31, 2, 53, 1, 7, 1, 1, 3, 3, 1, 7, 3, 4, 2, 6, 4, 13, 5, 3, 1, 7, 211, 13, 4, 1, 68, 1, 3, 2, 2, 1, 81, 3, 3714, 1, 1, 1, 25, 9, 6, 1, 5, 11, 84, 4, 2, 2, 2, 2, 90, 1, 3, 6, 40, 7379, 20902, 3162, 11172, 92, 2048, 8190, 2 };
        CHARFLAGS = buildBitFlags();
    }
}
