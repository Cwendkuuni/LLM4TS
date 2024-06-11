package com.pmdesigns.jvc.tools;

import java.util.*;
import java.io.*;

public class JVCParser implements JVCParserConstants
{
    private File template;
    private String templateName;
    private StringBuilder sb;
    private StringBuilder buf;
    private Set<String> imports;
    private boolean append;
    private int tagState;
    private boolean inCommentedTag;
    private int cacheBlockCount;
    private String cacheBlockKey;
    private int cacheBlockDepth;
    private String sbName;
    public int debugColumn;
    public JVCParserTokenManager token_source;
    SimpleCharStream jj_input_stream;
    public Token token;
    public Token jj_nt;
    private int jj_ntk;
    private int jj_gen;
    private final int[] jj_la1;
    private static int[] jj_la1_0;
    private List jj_expentries;
    private int[] jj_expentry;
    private int jj_kind;
    
    public static void main(final String[] args) throws FileNotFoundException, ParseException {
        if (args.length < 1 || args.length > 2) {
            System.out.println("usage: JVCParser <path to template file> [-debug]");
            return;
        }
        final boolean debug = args.length == 2 && "-debug".equals(args[1]);
        final TreeSet<String> imports = new TreeSet<String>();
        final JVCParser parser = new JVCParser(args[0], imports, debug);
        final String s = parser.parse();
        System.out.println("imports: " + imports + "\n");
        System.out.println(s);
    }
    
    public JVCParser(final String path, final Set<String> imports) throws FileNotFoundException {
        this(path, imports, false);
    }
    
    public JVCParser(final String path, final Set<String> imports, final boolean debug) throws FileNotFoundException {
        this(new FileReader(path));
        this.template = new File(path);
        this.imports = imports;
        if (debug) {
            this.templateName = this.template.getName();
        }
    }
    
    private void startTag(final Token t) throws ParseException {
        if (this.tagState == 0) {
            if (this.inCommentedTag) {
                throw new ParseException("Nested commented tag in " + this.template + ", line: " + t.beginLine);
            }
            this.inCommentedTag = true;
            this.buf.setLength(0);
        }
        else {
            if (this.tagState != 5) {
                throw new ParseException("Nested tag in " + this.template + ", line: " + t.beginLine);
            }
            this.tagState = this.token_source.curLexState;
            this.addEscaped(t.specialToken);
            if (this.buf.length() > 0) {
                if (!this.append) {
                    this.sb.append(this.sbName);
                    this.append = true;
                }
                this.escapeQuotes(this.buf);
                this.sb.append(".append(\"").append((CharSequence)this.buf).append("\")");
                this.buf.setLength(0);
            }
        }
    }
    
    private void endTag(final Token t) throws FileNotFoundException, ParseException {
        this.addEscaped(t.specialToken);
        switch (this.tagState) {
            case 4: {
                if (this.append) {
                    this.sb.append(";");
                    this.append = false;
                }
                if (this.isDebug()) {
                    this.sb.append(this.buf.toString());
                }
                else {
                    this.sb.append(this.buf.toString().trim());
                }
                if (t.image.length() > 2) {
                    this.addDebugging(this.sb, t.beginLine);
                    this.sb.append(t.image.substring(2));
                    break;
                }
                break;
            }
            case 2:
            case 3: {
                if (!this.append) {
                    this.sb.append(this.sbName);
                    this.append = true;
                }
                String expr = this.buf.toString().trim();
                if (expr.endsWith(";")) {
                    expr = expr.substring(0, expr.length() - 1);
                }
                int tagLen = 2;
                if (this.tagState == 3) {
                    this.sb.append(".append(_nf(").append(expr).append("))");
                }
                else {
                    tagLen = 3;
                    this.sb.append(".append(_hf(").append(expr).append("))");
                }
                if (t.image.length() > tagLen) {
                    this.sb.append(";");
                    this.append = false;
                    this.addDebugging(this.sb, t.beginLine);
                    this.sb.append(t.image.substring(tagLen));
                    break;
                }
                break;
            }
            case 1: {
                final String path = this.buf.toString().trim();
                if (path.length() == 0) {
                    throw new FileNotFoundException("Missing include file in " + this.template + ", line " + t.beginLine);
                }
                File f = new File(path);
                if (!f.exists()) {
                    File p = this.template.getAbsoluteFile();
                    while ((p = p.getParentFile()) != null) {
                        f = new File(p, path);
                        if (f.exists()) {
                            break;
                        }
                    }
                }
                if (!f.exists()) {
                    throw new FileNotFoundException("Can't find include file: " + path);
                }
                if (f.getName().charAt(0) != '_') {
                    throw new ParseException("Include file (" + f + ") does not begin with '_' character. In " + this.template + ", line " + t.beginLine);
                }
                if (this.append) {
                    this.sb.append(";");
                    this.append = false;
                }
                final JVCParser parser = new JVCParser(f.getAbsolutePath(), this.imports, this.isDebug());
                parser.cacheBlockDepth = this.cacheBlockDepth;
                parser.sbName = this.sbName;
                this.sb.append(parser.parse());
                break;
            }
            case 0: {
                if (this.inCommentedTag) {
                    this.inCommentedTag = false;
                    this.buf.setLength(0);
                    return;
                }
                break;
            }
            default: {
                throw new ParseException("Unbalanced end tag in " + this.template + ", line: " + t.beginLine);
            }
        }
        this.buf.setLength(0);
        this.tagState = 5;
    }
    
    private void startCache(final Token t) throws ParseException {
        if (this.cacheBlockDepth > 0) {
            System.out.println("Warn: nested cache block in " + this.template + ", line " + t.beginLine);
        }
        else {
            ++this.cacheBlockCount;
            if (this.append) {
                this.sb.append(";\n");
                this.append = false;
            }
            this.sb.append("{\n");
            this.sb.append("String _cached = getCachedBlock(\"");
            this.addCacheBlockKey(this.sb);
            this.sb.append("\");\n");
            this.sb.append("if (_cached == null) {\n");
            this.sb.append("StringBuilder _sb2 = new StringBuilder();\n");
            this.sbName = "_sb2";
        }
        ++this.cacheBlockDepth;
    }
    
    private void endCache(final Token t) throws ParseException {
        if (this.cacheBlockDepth <= 0) {
            throw new ParseException("Unbalanced cache block in " + this.template + ", line " + t.beginLine);
        }
        --this.cacheBlockDepth;
        if (this.cacheBlockDepth == 0) {
            if (this.append) {
                this.sb.append(";\n");
                this.append = false;
            }
            this.sb.append("_cached = _sb2.toString();\n");
            this.sb.append("setCachedBlock(\"");
            this.addCacheBlockKey(this.sb);
            this.sb.append("\", _cached);\n");
            this.sb.append("}\n");
            this.sb.append("_sb.append(_cached);\n");
            this.sb.append("}\n");
            this.sbName = "_sb";
        }
    }
    
    private void addCacheBlockKey(final StringBuilder sb) {
        this.cacheBlockKey = this.template.getAbsolutePath();
        sb.append(this.cacheBlockKey).append("_").append(this.cacheBlockCount);
    }
    
    private void addImport(final Token t) {
        this.addEscaped(t.specialToken);
        final String pkg = t.image.substring(7, t.image.length() - 1).trim();
        this.imports.add(pkg);
    }
    
    private void addEOL(final Token t) {
        this.addEscaped(t.specialToken);
        if (this.tagState != 5) {
            this.addDebugging(this.buf, t.beginLine);
            this.buf.append(t.image);
        }
        else {
            if (t.image.length() == 2) {
                this.buf.append("\\r\\n");
            }
            else {
                this.buf.append((t.image.charAt(0) == '\n') ? "\\n" : "\\r");
            }
            if (!this.append) {
                this.sb.append(this.sbName);
                this.append = true;
            }
            this.escapeQuotes(this.buf);
            this.sb.append(".append(\"").append((CharSequence)this.buf).append("\");");
            this.buf.setLength(0);
            this.append = false;
            this.addDebugging(this.sb, t.beginLine);
            this.sb.append(t.image);
        }
    }
    
    private boolean isDebug() {
        return this.templateName != null;
    }
    
    private void addDebugging(final StringBuilder sb, final int line) {
        if (this.isDebug()) {
            final int n = Math.max(sb.lastIndexOf("\n"), sb.lastIndexOf("\r"));
            for (int i = sb.length() - n; i < this.debugColumn; ++i) {
                sb.append(" ");
            }
            sb.append("\t// ").append(this.templateName).append(" (").append(line).append(")");
        }
    }
    
    private void addOther(final Token t) {
        this.addEscaped(t.specialToken);
        this.buf.append(t.image);
    }
    
    private void addEscaped(final Token t) {
        if (t != null) {
            this.addEscaped(t.specialToken);
            if (t.kind == 15) {
                this.buf.append(t.image.charAt(1));
            }
        }
    }
    
    private void flush() throws ParseException {
        if (this.tagState != 5) {
            throw new ParseException("Unbalanced tag at end of " + this.template);
        }
        if (this.buf.length() == 0) {
            if (this.append) {
                this.sb.append(";");
                this.append = false;
            }
        }
        else {
            if (!this.append) {
                this.sb.append(this.sbName);
                this.append = true;
            }
            this.escapeQuotes(this.buf);
            this.sb.append(".append(\"").append((CharSequence)this.buf).append("\");");
            this.buf.setLength(0);
            this.append = false;
        }
    }
    
    private void escapeQuotes(final StringBuilder sb) {
        for (int idx = sb.lastIndexOf("\""); idx >= 0; idx = sb.lastIndexOf("\"", idx)) {
            sb.insert(idx, '\\');
        }
    }
    
    public final String parse() throws ParseException, FileNotFoundException {
        while (true) {
            switch ((this.jj_ntk == -1) ? this.jj_ntk() : this.jj_ntk) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 16: {
                    switch ((this.jj_ntk == -1) ? this.jj_ntk() : this.jj_ntk) {
                        case 6: {
                            final Token t = this.jj_consume_token(6);
                            this.addEOL(t);
                            continue;
                        }
                        case 1: {
                            final Token t = this.jj_consume_token(1);
                            this.startTag(t);
                            continue;
                        }
                        case 2: {
                            final Token t = this.jj_consume_token(2);
                            this.startTag(t);
                            continue;
                        }
                        case 3: {
                            final Token t = this.jj_consume_token(3);
                            this.startTag(t);
                            continue;
                        }
                        case 4: {
                            final Token t = this.jj_consume_token(4);
                            this.startTag(t);
                            continue;
                        }
                        case 5: {
                            final Token t = this.jj_consume_token(5);
                            this.startTag(t);
                            continue;
                        }
                        case 9: {
                            final Token t = this.jj_consume_token(9);
                            this.endTag(t);
                            continue;
                        }
                        case 7: {
                            final Token t = this.jj_consume_token(7);
                            this.startCache(t);
                            continue;
                        }
                        case 8: {
                            final Token t = this.jj_consume_token(8);
                            this.endCache(t);
                            continue;
                        }
                        case 10: {
                            final Token t = this.jj_consume_token(10);
                            this.addImport(t);
                            continue;
                        }
                        case 16: {
                            final Token t = this.jj_consume_token(16);
                            this.addOther(t);
                            continue;
                        }
                        default: {
                            this.jj_la1[1] = this.jj_gen;
                            this.jj_consume_token(-1);
                            throw new ParseException();
                        }
                    }
                    break;
                }
                default: {
                    this.jj_la1[0] = this.jj_gen;
                    this.jj_consume_token(0);
                    this.flush();
                    return this.sb.toString();
                }
            }
        }
    }
    
    private static void jj_la1_init_0() {
        JVCParser.jj_la1_0 = new int[] { 67582, 67582 };
    }
    
    public JVCParser(final InputStream stream) {
        this(stream, null);
    }
    
    public JVCParser(final InputStream stream, final String encoding) {
        this.sb = new StringBuilder();
        this.buf = new StringBuilder();
        this.append = false;
        this.tagState = 5;
        this.inCommentedTag = false;
        this.cacheBlockCount = 0;
        this.cacheBlockDepth = 0;
        this.sbName = "_sb";
        this.debugColumn = 100;
        this.jj_la1 = new int[2];
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.token_source = new JVCParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 2; ++i) {
            this.jj_la1[i] = -1;
        }
    }
    
    public void ReInit(final InputStream stream) {
        this.ReInit(stream, null);
    }
    
    public void ReInit(final InputStream stream, final String encoding) {
        try {
            this.jj_input_stream.ReInit(stream, encoding, 1, 1);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 2; ++i) {
            this.jj_la1[i] = -1;
        }
    }
    
    public JVCParser(final Reader stream) {
        this.sb = new StringBuilder();
        this.buf = new StringBuilder();
        this.append = false;
        this.tagState = 5;
        this.inCommentedTag = false;
        this.cacheBlockCount = 0;
        this.cacheBlockDepth = 0;
        this.sbName = "_sb";
        this.debugColumn = 100;
        this.jj_la1 = new int[2];
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new JVCParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 2; ++i) {
            this.jj_la1[i] = -1;
        }
    }
    
    public void ReInit(final Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 2; ++i) {
            this.jj_la1[i] = -1;
        }
    }
    
    public JVCParser(final JVCParserTokenManager tm) {
        this.sb = new StringBuilder();
        this.buf = new StringBuilder();
        this.append = false;
        this.tagState = 5;
        this.inCommentedTag = false;
        this.cacheBlockCount = 0;
        this.cacheBlockDepth = 0;
        this.sbName = "_sb";
        this.debugColumn = 100;
        this.jj_la1 = new int[2];
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 2; ++i) {
            this.jj_la1[i] = -1;
        }
    }
    
    public void ReInit(final JVCParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 2; ++i) {
            this.jj_la1[i] = -1;
        }
    }
    
    private Token jj_consume_token(final int kind) throws ParseException {
        final Token oldToken;
        if ((oldToken = this.token).next != null) {
            this.token = this.token.next;
        }
        else {
            final Token token = this.token;
            final Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        if (this.token.kind == kind) {
            ++this.jj_gen;
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw this.generateParseException();
    }
    
    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        }
        else {
            final Token token = this.token;
            final Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        ++this.jj_gen;
        return this.token;
    }
    
    public final Token getToken(final int index) {
        Token t = this.token;
        for (int i = 0; i < index; ++i) {
            if (t.next != null) {
                t = t.next;
            }
            else {
                final Token token = t;
                final Token nextToken = this.token_source.getNextToken();
                token.next = nextToken;
                t = nextToken;
            }
        }
        return t;
    }
    
    private int jj_ntk() {
        final Token next = this.token.next;
        this.jj_nt = next;
        if (next == null) {
            final Token token = this.token;
            final Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            return this.jj_ntk = nextToken.kind;
        }
        return this.jj_ntk = this.jj_nt.kind;
    }
    
    public ParseException generateParseException() {
        this.jj_expentries.clear();
        final boolean[] la1tokens = new boolean[17];
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int i = 0; i < 2; ++i) {
            if (this.jj_la1[i] == this.jj_gen) {
                for (int j = 0; j < 32; ++j) {
                    if ((JVCParser.jj_la1_0[i] & 1 << j) != 0x0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 17; ++i) {
            if (la1tokens[i]) {
                (this.jj_expentry = new int[1])[0] = i;
                this.jj_expentries.add(this.jj_expentry);
            }
        }
        final int[][] exptokseq = new int[this.jj_expentries.size()][];
        for (int k = 0; k < this.jj_expentries.size(); ++k) {
            exptokseq[k] = this.jj_expentries.get(k);
        }
        return new ParseException(this.token, exptokseq, JVCParser.tokenImage);
    }
    
    public final void enable_tracing() {
    }
    
    public final void disable_tracing() {
    }
    
    static {
        jj_la1_init_0();
    }
}
