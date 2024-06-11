package com.werken.saxpath;

class XPathLexer
{
    private String xpath;
    private int currentPosition;
    private int endPosition;
    private Token previousToken;
    
    public XPathLexer(final String xpath) {
        this.setXPath(xpath);
    }
    
    XPathLexer() {
    }
    
    void setXPath(final String xpath) {
        this.xpath = xpath;
        this.currentPosition = 0;
        this.endPosition = xpath.length();
    }
    
    public String getXPath() {
        return this.xpath;
    }
    
    public Token nextToken() {
        Token token = null;
        do {
            token = null;
            Label_0769: {
                switch (this.LA(1)) {
                    case '$': {
                        token = this.dollar();
                        break;
                    }
                    case '\"':
                    case '\'': {
                        token = this.literal();
                        break;
                    }
                    case '/': {
                        token = this.slashes();
                        break;
                    }
                    case ',': {
                        token = this.comma();
                        break;
                    }
                    case '(': {
                        token = this.leftParen();
                        break;
                    }
                    case ')': {
                        token = this.rightParen();
                        break;
                    }
                    case '[': {
                        token = this.leftBracket();
                        break;
                    }
                    case ']': {
                        token = this.rightBracket();
                        break;
                    }
                    case '+': {
                        token = this.plus();
                        break;
                    }
                    case '-': {
                        token = this.minus();
                        break;
                    }
                    case '<':
                    case '>': {
                        token = this.relationalOperator();
                        break;
                    }
                    case '=': {
                        token = this.equals();
                        break;
                    }
                    case '!': {
                        if (this.LA(2) == '=') {
                            token = this.notEquals();
                            break;
                        }
                        token = this.not();
                        break;
                    }
                    case '|': {
                        token = this.pipe();
                        break;
                    }
                    case '@': {
                        token = this.at();
                        break;
                    }
                    case ':': {
                        if (this.LA(2) == ':') {
                            token = this.doubleColon();
                            break;
                        }
                        token = this.colon();
                        break;
                    }
                    case '*': {
                        token = this.star();
                        break;
                    }
                    case '.': {
                        switch (this.LA(2)) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9': {
                                token = this.number();
                                break Label_0769;
                            }
                            default: {
                                token = this.dots();
                                break Label_0769;
                            }
                        }
                        break;
                    }
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        token = this.number();
                        break;
                    }
                    case '\t':
                    case '\n':
                    case '\r':
                    case ' ': {
                        token = this.whitespace();
                        break;
                    }
                    default: {
                        if (this.isIdentifierStartChar(this.LA(1))) {
                            token = this.identifierOrOperatorName();
                            break;
                        }
                        break;
                    }
                }
            }
            if (token == null) {
                token = new Token(-1, this.getXPath(), this.currentPosition(), this.endPosition());
            }
        } while (token.getTokenType() == -2);
        this.setPreviousToken(token);
        return token;
    }
    
    Token identifierOrOperatorName() {
        Token token = null;
        final Token previousToken = this.getPreviousToken();
        if (previousToken != null) {
            switch (previousToken.getTokenType()) {
                case 1:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 24:
                case 25:
                case 26:
                case 28:
                case 29:
                case 32: {
                    token = this.identifier();
                    break;
                }
                default: {
                    token = this.operatorName();
                    break;
                }
            }
        }
        else {
            token = this.identifier();
        }
        return token;
    }
    
    Token identifier() {
        Token token = null;
        final int start = this.currentPosition();
        while (this.hasMoreChars() && this.isIdentifierChar(this.LA(1))) {
            this.consume();
        }
        token = new Token(15, this.getXPath(), start, this.currentPosition());
        return token;
    }
    
    Token operatorName() {
        Token token = null;
        switch (this.LA(1)) {
            case 'a': {
                token = this.and();
                break;
            }
            case 'o': {
                token = this.or();
                break;
            }
            case 'm': {
                token = this.mod();
                break;
            }
            case 'd': {
                token = this.div();
                break;
            }
        }
        return token;
    }
    
    Token mod() {
        Token token = null;
        if (this.LA(1) == 'm' && this.LA(2) == 'o' && this.LA(3) == 'd' && !this.isIdentifierChar(this.LA(4))) {
            token = new Token(25, this.getXPath(), this.currentPosition(), this.currentPosition() + 3);
            this.consume();
            this.consume();
            this.consume();
        }
        return token;
    }
    
    Token div() {
        Token token = null;
        if (this.LA(1) == 'd' && this.LA(2) == 'i' && this.LA(3) == 'v' && !this.isIdentifierChar(this.LA(4))) {
            token = new Token(24, this.getXPath(), this.currentPosition(), this.currentPosition() + 3);
            this.consume();
            this.consume();
            this.consume();
        }
        return token;
    }
    
    Token and() {
        Token token = null;
        if (this.LA(1) == 'a' && this.LA(2) == 'n' && this.LA(3) == 'd' && !this.isIdentifierChar(this.LA(4))) {
            token = new Token(28, this.getXPath(), this.currentPosition(), this.currentPosition() + 3);
            this.consume();
            this.consume();
            this.consume();
        }
        return token;
    }
    
    Token or() {
        Token token = null;
        if (this.LA(1) == 'o' && this.LA(2) == 'r' && !this.isIdentifierChar(this.LA(3))) {
            token = new Token(29, this.getXPath(), this.currentPosition(), this.currentPosition() + 2);
            this.consume();
            this.consume();
        }
        return token;
    }
    
    Token number() {
        final int start = this.currentPosition();
        boolean periodAllowed = true;
    Label_0099:
        while (true) {
            switch (this.LA(1)) {
                case '.': {
                    if (periodAllowed) {
                        periodAllowed = false;
                        this.consume();
                        continue;
                    }
                    break Label_0099;
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    this.consume();
                    continue;
                }
                default: {
                    break Label_0099;
                }
            }
        }
        Token token = null;
        if (periodAllowed) {
            token = new Token(30, this.getXPath(), start, this.currentPosition());
        }
        else {
            token = new Token(31, this.getXPath(), start, this.currentPosition());
        }
        return token;
    }
    
    Token whitespace() {
        this.consume();
    Label_0070:
        while (this.hasMoreChars()) {
            switch (this.LA(1)) {
                case '\t':
                case '\n':
                case '\r':
                case ' ': {
                    this.consume();
                    continue;
                }
                default: {
                    break Label_0070;
                }
            }
        }
        return new Token(-2, this.getXPath(), 0, 0);
    }
    
    Token comma() {
        final Token token = new Token(32, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token equals() {
        final Token token = new Token(21, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token minus() {
        final Token token = new Token(6, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token plus() {
        final Token token = new Token(5, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token dollar() {
        final Token token = new Token(26, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token pipe() {
        final Token token = new Token(17, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token at() {
        final Token token = new Token(16, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token colon() {
        final Token token = new Token(18, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token doubleColon() {
        final Token token = new Token(19, this.getXPath(), this.currentPosition(), this.currentPosition() + 2);
        this.consume();
        this.consume();
        return token;
    }
    
    Token not() {
        final Token token = new Token(23, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token notEquals() {
        final Token token = new Token(22, this.getXPath(), this.currentPosition(), this.currentPosition() + 2);
        this.consume();
        this.consume();
        return token;
    }
    
    Token relationalOperator() {
        Token token = null;
        switch (this.LA(1)) {
            case '<': {
                if (this.LA(2) == '=') {
                    token = new Token(8, this.getXPath(), this.currentPosition(), this.currentPosition() + 2);
                    this.consume();
                }
                else {
                    token = new Token(7, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
                }
                this.consume();
                break;
            }
            case '>': {
                if (this.LA(2) == '=') {
                    token = new Token(10, this.getXPath(), this.currentPosition(), this.currentPosition() + 2);
                    this.consume();
                }
                else {
                    token = new Token(9, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
                }
                this.consume();
                break;
            }
        }
        return token;
    }
    
    Token star() {
        final Token token = new Token(20, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token literal() {
        Token token = null;
        final char match = this.LA(1);
        this.consume();
        final int start = this.currentPosition();
        while (token == null && this.hasMoreChars()) {
            if (this.LA(1) == match) {
                token = new Token(27, this.getXPath(), start, this.currentPosition());
            }
            this.consume();
        }
        return token;
    }
    
    Token dots() {
        Token token = null;
        switch (this.LA(2)) {
            case '.': {
                token = new Token(14, this.getXPath(), this.currentPosition(), this.currentPosition() + 2);
                this.consume();
                this.consume();
                break;
            }
            default: {
                token = new Token(13, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
                this.consume();
                break;
            }
        }
        return token;
    }
    
    Token leftBracket() {
        final Token token = new Token(3, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token rightBracket() {
        final Token token = new Token(4, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token leftParen() {
        final Token token = new Token(1, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token rightParen() {
        final Token token = new Token(2, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
        this.consume();
        return token;
    }
    
    Token slashes() {
        Token token = null;
        switch (this.LA(2)) {
            case '/': {
                token = new Token(12, this.getXPath(), this.currentPosition(), this.currentPosition() + 2);
                this.consume();
                this.consume();
                break;
            }
            default: {
                token = new Token(11, this.getXPath(), this.currentPosition(), this.currentPosition() + 1);
                this.consume();
                break;
            }
        }
        return token;
    }
    
    char LA(final int i) {
        if (this.currentPosition + (i - 1) >= this.endPosition()) {
            return '\uffff';
        }
        return this.getXPath().charAt(this.currentPosition() + (i - 1));
    }
    
    void consume() {
        ++this.currentPosition;
    }
    
    void consume(final int i) {
        this.currentPosition += i;
    }
    
    int currentPosition() {
        return this.currentPosition;
    }
    
    int endPosition() {
        return this.endPosition;
    }
    
    Token getPreviousToken() {
        return this.previousToken;
    }
    
    void setPreviousToken(final Token previousToken) {
        this.previousToken = previousToken;
    }
    
    boolean hasMoreChars() {
        return this.currentPosition() < this.endPosition();
    }
    
    boolean isIdentifierChar(final char c) {
        switch (c) {
            case '-':
            case '.': {
                return true;
            }
            default: {
                return Character.isUnicodeIdentifierPart(c);
            }
        }
    }
    
    boolean isIdentifierStartChar(final char c) {
        return c == '_' || Character.isUnicodeIdentifierStart(c);
    }
}
