package org.znerd.xmlenc;

import java.io.*;

public class XMLEncoder
{
    private static final char[] DECLARATION_START;
    private static final int DECLARATION_START_LENGTH;
    private static final char[] DECLARATION_END;
    private static final int DECLARATION_END_LENGTH;
    private static final char[] ESC_GREATER_THAN;
    private static final char[] ESC_LESS_THAN;
    private static final char[] ESC_AMPERSAND;
    private static final char[] ESC_APOSTROPHE;
    private static final char[] ESC_QUOTE;
    private static final char[] AMPERSAND_HASH;
    private static final char[] EQUALS_APOSTROPHE;
    private static final char[] EQUALS_QUOTE;
    private final String _encoding;
    private final char[] _encodingCharArray;
    private final boolean _sevenBitEncoding;
    
    public static final XMLEncoder getEncoder(final String encoding) throws IllegalArgumentException, UnsupportedEncodingException {
        return new XMLEncoder(encoding);
    }
    
    public XMLEncoder(final String encoding) throws IllegalArgumentException, UnsupportedEncodingException {
        if (encoding == null) {
            throw new IllegalArgumentException("encoding == null");
        }
        final String ucEncoding = encoding.toUpperCase();
        if (ucEncoding.equals("UTF-8") || ucEncoding.equals("UTF-16")) {
            this._sevenBitEncoding = false;
        }
        else {
            if (!ucEncoding.equals("US-ASCII") && !ucEncoding.equals("ASCII") && !ucEncoding.startsWith("ISO-8859-")) {
                throw new UnsupportedEncodingException(encoding);
            }
            this._sevenBitEncoding = true;
        }
        this._encoding = encoding;
        this._encodingCharArray = encoding.toCharArray();
    }
    
    public String getEncoding() {
        return this._encoding;
    }
    
    public void declaration(final Writer out) throws NullPointerException, IOException {
        out.write(XMLEncoder.DECLARATION_START, 0, XMLEncoder.DECLARATION_START_LENGTH);
        out.write(this._encodingCharArray);
        out.write(XMLEncoder.DECLARATION_END, 0, XMLEncoder.DECLARATION_END_LENGTH);
    }
    
    public void text(final Writer out, final String text, final boolean escapeAmpersands) throws NullPointerException, InvalidXMLException, IOException {
        this.text(out, text.toCharArray(), 0, text.length(), escapeAmpersands);
    }
    
    public void text(final Writer out, final char[] ch, final int start, final int length, final boolean escapeAmpersands) throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        final int end = start + length;
        int lastEscaped = start;
        for (int i = start; i < end; ++i) {
            final int c = ch[i];
            if ((c < 63 || c > 127) && (c < 39 || c > 59) && (c < 32 || c > 37) && (c != 38 || escapeAmpersands) && (c <= 127 || this._sevenBitEncoding) && c != 10 && c != 13 && c != 61) {
                if (c != 9) {
                    out.write(ch, lastEscaped, i - lastEscaped);
                    if (c == 60) {
                        out.write(XMLEncoder.ESC_LESS_THAN, 0, 4);
                    }
                    else if (c == 62) {
                        out.write(XMLEncoder.ESC_GREATER_THAN, 0, 4);
                    }
                    else if (c == 38) {
                        out.write(XMLEncoder.ESC_AMPERSAND, 0, 5);
                    }
                    else {
                        if (c <= 127) {
                            throw new InvalidXMLException("The character 0x" + Integer.toHexString(c) + " is not valid.");
                        }
                        out.write(XMLEncoder.AMPERSAND_HASH, 0, 2);
                        out.write(Integer.toString(c));
                        out.write(59);
                    }
                    lastEscaped = i + 1;
                }
            }
        }
        out.write(ch, lastEscaped, end - lastEscaped);
    }
    
    public void text(final Writer out, final char c) throws InvalidXMLException, IOException {
        if ((c >= '?' && c <= '\u007f') || (c >= '\'' && c <= ';') || (c >= ' ' && c <= '%') || c == '&' || (c > '\u007f' && !this._sevenBitEncoding) || c == '\n' || c == '\r' || c == '=' || c == '\t') {
            out.write(c);
        }
        else if (c == '<') {
            out.write(XMLEncoder.ESC_LESS_THAN, 0, 4);
        }
        else if (c == '>') {
            out.write(XMLEncoder.ESC_GREATER_THAN, 0, 4);
        }
        else {
            if (c <= '\u007f') {
                throw new InvalidXMLException("The character 0x" + Integer.toHexString(c) + " is not valid.");
            }
            out.write(XMLEncoder.AMPERSAND_HASH, 0, 2);
            out.write(Integer.toString(c));
            out.write(59);
        }
    }
    
    public void text(final Writer out, final char c, final boolean escapeAmpersands) throws InvalidXMLException, IOException {
        if ((c >= '?' && c <= '\u007f') || (c >= '\'' && c <= ';') || (c >= ' ' && c <= '%') || (c == '&' && escapeAmpersands) || (c > '\u007f' && !this._sevenBitEncoding) || c == '\n' || c == '\r' || c == '=' || c == '\t') {
            out.write(c);
        }
        else if (c == '<') {
            out.write(XMLEncoder.ESC_LESS_THAN, 0, 4);
        }
        else if (c == '>') {
            out.write(XMLEncoder.ESC_GREATER_THAN, 0, 4);
        }
        else if (c == '&') {
            out.write(XMLEncoder.ESC_AMPERSAND, 0, 5);
        }
        else {
            if (c <= '\u007f') {
                throw new InvalidXMLException("The character 0x" + Integer.toHexString(c) + " is not valid.");
            }
            out.write(XMLEncoder.AMPERSAND_HASH, 0, 2);
            out.write(Integer.toString(c));
            out.write(59);
        }
    }
    
    public void whitespace(final Writer out, final String s) throws NullPointerException, InvalidXMLException, IOException {
        final char[] ch = s.toCharArray();
        final int length = ch.length;
        this.whitespace(out, ch, 0, length);
    }
    
    public void whitespace(final Writer out, final char[] ch, final int start, final int length) throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        XMLChecker.checkS(ch, start, length);
        out.write(ch, start, length);
    }
    
    public void attribute(final Writer out, final String name, final String value, final char quotationMark, final boolean escapeAmpersands) throws NullPointerException, IOException {
        final char[] ch = value.toCharArray();
        final int length = ch.length;
        final int start = 0;
        final int end = start + length;
        int lastEscaped = 0;
        boolean useQuote;
        if (quotationMark == '\"') {
            useQuote = true;
        }
        else {
            if (quotationMark != '\'') {
                final String error = "Character 0x" + Integer.toHexString(quotationMark) + " ('" + quotationMark + "') is not a valid quotation mark.";
                throw new IllegalArgumentException(error);
            }
            useQuote = false;
        }
        out.write(32);
        out.write(name);
        if (useQuote) {
            out.write(XMLEncoder.EQUALS_QUOTE, 0, 2);
        }
        else {
            out.write(XMLEncoder.EQUALS_APOSTROPHE, 0, 2);
        }
        for (int i = start; i < end; ++i) {
            final int c = ch[i];
            if ((c < 63 || c > 127) && (c < 40 || c > 59) && (c < 32 || c > 37 || c == 34) && (c != 38 || escapeAmpersands) && (c <= 127 || this._sevenBitEncoding) && (useQuote || c != 34) && (!useQuote || c != 39) && c != 10 && c != 13 && c != 61) {
                if (c != 9) {
                    out.write(ch, lastEscaped, i - lastEscaped);
                    if (c == 60) {
                        out.write(XMLEncoder.ESC_LESS_THAN, 0, 4);
                    }
                    else if (c == 62) {
                        out.write(XMLEncoder.ESC_GREATER_THAN, 0, 4);
                    }
                    else if (c == 34) {
                        out.write(XMLEncoder.ESC_QUOTE, 0, 6);
                    }
                    else if (c == 39) {
                        out.write(XMLEncoder.ESC_APOSTROPHE, 0, 6);
                    }
                    else if (c == 38) {
                        out.write(XMLEncoder.ESC_AMPERSAND, 0, 5);
                    }
                    else {
                        if (c <= 127) {
                            throw new InvalidXMLException("The character 0x" + Integer.toHexString(c) + " is not valid.");
                        }
                        out.write(XMLEncoder.AMPERSAND_HASH, 0, 2);
                        out.write(Integer.toString(c));
                        out.write(59);
                    }
                    lastEscaped = i + 1;
                }
            }
        }
        out.write(ch, lastEscaped, length - lastEscaped);
        out.write(quotationMark);
    }
    
    static {
        DECLARATION_START = "<?xml version=\"1.0\" encoding=\"".toCharArray();
        DECLARATION_START_LENGTH = XMLEncoder.DECLARATION_START.length;
        DECLARATION_END = "\"?>".toCharArray();
        DECLARATION_END_LENGTH = XMLEncoder.DECLARATION_END.length;
        ESC_GREATER_THAN = new char[] { '&', 'g', 't', ';' };
        ESC_LESS_THAN = new char[] { '&', 'l', 't', ';' };
        ESC_AMPERSAND = new char[] { '&', 'a', 'm', 'p', ';' };
        ESC_APOSTROPHE = new char[] { '&', 'a', 'p', 'o', 's', ';' };
        ESC_QUOTE = new char[] { '&', 'q', 'u', 'o', 't', ';' };
        AMPERSAND_HASH = new char[] { '&', '#' };
        EQUALS_APOSTROPHE = new char[] { '=', '\'' };
        EQUALS_QUOTE = new char[] { '=', '\"' };
    }
}
