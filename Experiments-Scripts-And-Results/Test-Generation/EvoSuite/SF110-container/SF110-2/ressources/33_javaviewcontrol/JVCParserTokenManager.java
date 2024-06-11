package com.pmdesigns.jvc.tools;

import java.io.*;

public class JVCParserTokenManager implements JVCParserConstants
{
    public PrintStream debugStream;
    static final long[] jjbitVec0;
    static final int[] jjnextStates;
    public static final String[] jjstrLiteralImages;
    public static final String[] lexStateNames;
    public static final int[] jjnewLexState;
    static final long[] jjtoToken;
    static final long[] jjtoSkip;
    static final long[] jjtoSpecial;
    protected SimpleCharStream input_stream;
    private final int[] jjrounds;
    private final int[] jjstateSet;
    protected char curChar;
    int curLexState;
    int defaultLexState;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;
    
    public void setDebugStream(final PrintStream ds) {
        this.debugStream = ds;
    }
    
    private final int jjStopStringLiteralDfa_5(final int pos, final long active0) {
        switch (pos) {
            case 0: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 11;
                }
                return -1;
            }
            case 1: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 41;
                }
                return -1;
            }
            default: {
                return -1;
            }
        }
    }
    
    private final int jjStartNfa_5(final int pos, final long active0) {
        return this.jjMoveNfa_5(this.jjStopStringLiteralDfa_5(pos, active0), pos + 1);
    }
    
    private int jjStopAtPos(final int pos, final int kind) {
        this.jjmatchedKind = kind;
        return (this.jjmatchedPos = pos) + 1;
    }
    
    private int jjMoveStringLiteralDfa0_5() {
        switch (this.curChar) {
            case '[': {
                return this.jjMoveStringLiteralDfa1_5(62L);
            }
            default: {
                return this.jjMoveNfa_5(0, 0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa1_5(final long active0) {
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_5(0, active0);
            return 1;
        }
        switch (this.curChar) {
            case '[': {
                if ((active0 & 0x2L) != 0x0L) {
                    this.jjmatchedKind = 1;
                    this.jjmatchedPos = 1;
                }
                return this.jjMoveStringLiteralDfa2_5(active0, 60L);
            }
            default: {
                return this.jjStartNfa_5(0, active0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa2_5(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_5(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_5(1, active0);
            return 2;
        }
        switch (this.curChar) {
            case '!': {
                if ((active0 & 0x20L) != 0x0L) {
                    return this.jjStopAtPos(2, 5);
                }
                break;
            }
            case '+': {
                if ((active0 & 0x10L) != 0x0L) {
                    return this.jjStopAtPos(2, 4);
                }
                break;
            }
            case '=': {
                if ((active0 & 0x4L) != 0x0L) {
                    this.jjmatchedKind = 2;
                    this.jjmatchedPos = 2;
                }
                return this.jjMoveStringLiteralDfa3_5(active0, 8L);
            }
        }
        return this.jjStartNfa_5(1, active0);
    }
    
    private int jjMoveStringLiteralDfa3_5(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_5(1, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_5(2, active0);
            return 3;
        }
        switch (this.curChar) {
            case '=': {
                if ((active0 & 0x8L) != 0x0L) {
                    return this.jjStopAtPos(3, 3);
                }
                break;
            }
        }
        return this.jjStartNfa_5(2, active0);
    }
    
    private int jjMoveNfa_5(final int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 41;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            if (++this.jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            if (this.curChar < '@') {
                final long l = 1L << this.curChar;
                do {
                    switch (this.jjstateSet[--i]) {
                        case 41: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                            }
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if ((0x2400L & l) != 0x0L && kind > 6) {
                                kind = 6;
                            }
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 1: {
                            if (this.curChar == '\n' && kind > 6) {
                                kind = 6;
                                continue;
                            }
                            continue;
                        }
                        case 2: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 4: {
                            if ((0x2400L & l) != 0x0L && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 5: {
                            if (this.curChar == '\n' && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 6: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 5;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 12: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 14: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 28: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                                continue;
                            }
                            continue;
                        }
                        case 30: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else if (this.curChar < '\u0080') {
                final long l = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 11: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                            }
                            if (this.curChar == '[') {
                                this.jjAddStates(6, 7);
                                continue;
                            }
                            continue;
                        }
                        case 41: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 3: {
                            if (this.curChar != ']') {
                                continue;
                            }
                            if (kind > 9) {
                                kind = 9;
                            }
                            this.jjAddStates(10, 11);
                            continue;
                        }
                        case 7: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 8: {
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 10: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            continue;
                        }
                        case 13: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 15: {
                            if (this.curChar == ']' && kind > 7) {
                                kind = 7;
                                continue;
                            }
                            continue;
                        }
                        case 16: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 15;
                                continue;
                            }
                            continue;
                        }
                        case 17: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 13;
                                continue;
                            }
                            continue;
                        }
                        case 18: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 17;
                                continue;
                            }
                            continue;
                        }
                        case 19: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 18;
                                continue;
                            }
                            continue;
                        }
                        case 20: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 19;
                                continue;
                            }
                            continue;
                        }
                        case 21: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 20;
                                continue;
                            }
                            continue;
                        }
                        case 22: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 21;
                                continue;
                            }
                            continue;
                        }
                        case 23: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 22;
                                continue;
                            }
                            continue;
                        }
                        case 24: {
                            if (this.curChar == 'g') {
                                this.jjstateSet[this.jjnewStateCnt++] = 23;
                                continue;
                            }
                            continue;
                        }
                        case 25: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 24;
                                continue;
                            }
                            continue;
                        }
                        case 26: {
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 27: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                                continue;
                            }
                            continue;
                        }
                        case 29: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        case 31: {
                            if (this.curChar == ']' && kind > 8) {
                                kind = 8;
                                continue;
                            }
                            continue;
                        }
                        case 32: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 31;
                                continue;
                            }
                            continue;
                        }
                        case 33: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 29;
                                continue;
                            }
                            continue;
                        }
                        case 34: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                                continue;
                            }
                            continue;
                        }
                        case 35: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 34;
                                continue;
                            }
                            continue;
                        }
                        case 36: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 35;
                                continue;
                            }
                            continue;
                        }
                        case 37: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 36;
                                continue;
                            }
                            continue;
                        }
                        case 38: {
                            if (this.curChar == 'd') {
                                this.jjstateSet[this.jjnewStateCnt++] = 37;
                                continue;
                            }
                            continue;
                        }
                        case 39: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 38;
                                continue;
                            }
                            continue;
                        }
                        case 40: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else {
                final int i2 = (this.curChar & '\u00ff') >> 6;
                final long l2 = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 9: {
                            if ((JVCParserTokenManager.jjbitVec0[i2] & l2) != 0x0L && kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            ++curPos;
            final int n = i = this.jjnewStateCnt;
            final int n2 = 41;
            final int jjnewStateCnt = startsAt;
            this.jjnewStateCnt = jjnewStateCnt;
            if (n == (startsAt = n2 - jjnewStateCnt)) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException e) {
                return curPos;
            }
        }
        return curPos;
    }
    
    private final int jjStopStringLiteralDfa_4(final int pos, final long active0) {
        switch (pos) {
            case 0: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 35;
                }
                return -1;
            }
            case 1: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 65;
                }
                return -1;
            }
            default: {
                return -1;
            }
        }
    }
    
    private final int jjStartNfa_4(final int pos, final long active0) {
        return this.jjMoveNfa_4(this.jjStopStringLiteralDfa_4(pos, active0), pos + 1);
    }
    
    private int jjMoveStringLiteralDfa0_4() {
        switch (this.curChar) {
            case '[': {
                return this.jjMoveStringLiteralDfa1_4(62L);
            }
            default: {
                return this.jjMoveNfa_4(0, 0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa1_4(final long active0) {
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_4(0, active0);
            return 1;
        }
        switch (this.curChar) {
            case '[': {
                if ((active0 & 0x2L) != 0x0L) {
                    this.jjmatchedKind = 1;
                    this.jjmatchedPos = 1;
                }
                return this.jjMoveStringLiteralDfa2_4(active0, 60L);
            }
            default: {
                return this.jjStartNfa_4(0, active0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa2_4(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_4(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_4(1, active0);
            return 2;
        }
        switch (this.curChar) {
            case '!': {
                if ((active0 & 0x20L) != 0x0L) {
                    return this.jjStopAtPos(2, 5);
                }
                break;
            }
            case '+': {
                if ((active0 & 0x10L) != 0x0L) {
                    return this.jjStopAtPos(2, 4);
                }
                break;
            }
            case '=': {
                if ((active0 & 0x4L) != 0x0L) {
                    this.jjmatchedKind = 2;
                    this.jjmatchedPos = 2;
                }
                return this.jjMoveStringLiteralDfa3_4(active0, 8L);
            }
        }
        return this.jjStartNfa_4(1, active0);
    }
    
    private int jjMoveStringLiteralDfa3_4(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_4(1, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_4(2, active0);
            return 3;
        }
        switch (this.curChar) {
            case '=': {
                if ((active0 & 0x8L) != 0x0L) {
                    return this.jjStopAtPos(3, 3);
                }
                break;
            }
        }
        return this.jjStartNfa_4(2, active0);
    }
    
    private int jjMoveNfa_4(final int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 65;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            if (++this.jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            if (this.curChar < '@') {
                final long l = 1L << this.curChar;
                do {
                    switch (this.jjstateSet[--i]) {
                        case 65: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(52, 64);
                            }
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(36, 50);
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if ((0x2400L & l) != 0x0L && kind > 6) {
                                kind = 6;
                            }
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 1: {
                            if (this.curChar == '\n' && kind > 6) {
                                kind = 6;
                                continue;
                            }
                            continue;
                        }
                        case 2: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 4: {
                            if ((0x2400L & l) != 0x0L && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 5: {
                            if (this.curChar == '\n' && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 6: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 5;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddStates(12, 15);
                                continue;
                            }
                            continue;
                        }
                        case 10: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(10, 26);
                                continue;
                            }
                            continue;
                        }
                        case 12: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 13);
                                continue;
                            }
                            continue;
                        }
                        case 14: {
                            if ((0x3FF000000000000L & l) != 0x0L) {
                                this.jjCheckNAddStates(16, 20);
                                continue;
                            }
                            continue;
                        }
                        case 15: {
                            if (this.curChar == '.') {
                                this.jjstateSet[this.jjnewStateCnt++] = 16;
                                continue;
                            }
                            continue;
                        }
                        case 17: {
                            if ((0x3FF000000000000L & l) != 0x0L) {
                                this.jjCheckNAddStates(21, 25);
                                continue;
                            }
                            continue;
                        }
                        case 18: {
                            if (this.curChar == '*') {
                                this.jjCheckNAddTwoStates(19, 20);
                                continue;
                            }
                            continue;
                        }
                        case 19: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(19, 20);
                                continue;
                            }
                            continue;
                        }
                        case 20: {
                            if (this.curChar == ';' && kind > 10) {
                                kind = 10;
                                continue;
                            }
                            continue;
                        }
                        case 21: {
                            if (this.curChar == '.') {
                                this.jjstateSet[this.jjnewStateCnt++] = 18;
                                continue;
                            }
                            continue;
                        }
                        case 33: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 36: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(36, 50);
                                continue;
                            }
                            continue;
                        }
                        case 38: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(26, 27);
                                continue;
                            }
                            continue;
                        }
                        case 52: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(52, 64);
                                continue;
                            }
                            continue;
                        }
                        case 54: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(28, 29);
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else if (this.curChar < '\u0080') {
                final long l = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 65: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 63;
                                continue;
                            }
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 49;
                                continue;
                            }
                            continue;
                        }
                        case 35: {
                            if (this.curChar == '[') {
                                this.jjAddStates(30, 31);
                            }
                            if (this.curChar == '[') {
                                this.jjAddStates(32, 33);
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if (this.curChar == '[') {
                                this.jjAddStates(34, 35);
                                continue;
                            }
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                                continue;
                            }
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 30;
                                continue;
                            }
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 3: {
                            if (this.curChar != ']') {
                                continue;
                            }
                            if (kind > 9) {
                                kind = 9;
                            }
                            this.jjAddStates(10, 11);
                            continue;
                        }
                        case 7: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 8: {
                            if (this.curChar == 't') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            continue;
                        }
                        case 11: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 12;
                                continue;
                            }
                            continue;
                        }
                        case 13:
                        case 14: {
                            if ((0x7FFFFFE87FFFFFEL & l) != 0x0L) {
                                this.jjCheckNAddStates(16, 20);
                                continue;
                            }
                            continue;
                        }
                        case 16:
                        case 17: {
                            if ((0x7FFFFFE87FFFFFEL & l) != 0x0L) {
                                this.jjCheckNAddStates(21, 25);
                                continue;
                            }
                            continue;
                        }
                        case 22: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 11;
                                continue;
                            }
                            continue;
                        }
                        case 23: {
                            if (this.curChar == 't') {
                                this.jjstateSet[this.jjnewStateCnt++] = 22;
                                continue;
                            }
                            continue;
                        }
                        case 24: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 23;
                                continue;
                            }
                            continue;
                        }
                        case 25: {
                            if (this.curChar == 't') {
                                this.jjstateSet[this.jjnewStateCnt++] = 24;
                                continue;
                            }
                            continue;
                        }
                        case 26: {
                            if (this.curChar == 's') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 27: {
                            if (this.curChar == 'r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 8;
                                continue;
                            }
                            continue;
                        }
                        case 28: {
                            if (this.curChar == 'o') {
                                this.jjstateSet[this.jjnewStateCnt++] = 27;
                                continue;
                            }
                            continue;
                        }
                        case 29: {
                            if (this.curChar == 'p') {
                                this.jjstateSet[this.jjnewStateCnt++] = 28;
                                continue;
                            }
                            continue;
                        }
                        case 30: {
                            if (this.curChar == 'm') {
                                this.jjstateSet[this.jjnewStateCnt++] = 29;
                                continue;
                            }
                            continue;
                        }
                        case 31: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 30;
                                continue;
                            }
                            continue;
                        }
                        case 32: {
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                                continue;
                            }
                            continue;
                        }
                        case 33: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 34: {
                            if (this.curChar == '[') {
                                this.jjAddStates(34, 35);
                                continue;
                            }
                            continue;
                        }
                        case 37: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(26, 27);
                                continue;
                            }
                            continue;
                        }
                        case 39: {
                            if (this.curChar == ']' && kind > 7) {
                                kind = 7;
                                continue;
                            }
                            continue;
                        }
                        case 40: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            continue;
                        }
                        case 41: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 37;
                                continue;
                            }
                            continue;
                        }
                        case 42: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 41;
                                continue;
                            }
                            continue;
                        }
                        case 43: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 42;
                                continue;
                            }
                            continue;
                        }
                        case 44: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 43;
                                continue;
                            }
                            continue;
                        }
                        case 45: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 44;
                                continue;
                            }
                            continue;
                        }
                        case 46: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 45;
                                continue;
                            }
                            continue;
                        }
                        case 47: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 46;
                                continue;
                            }
                            continue;
                        }
                        case 48: {
                            if (this.curChar == 'g') {
                                this.jjstateSet[this.jjnewStateCnt++] = 47;
                                continue;
                            }
                            continue;
                        }
                        case 49: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 48;
                                continue;
                            }
                            continue;
                        }
                        case 50: {
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 49;
                                continue;
                            }
                            continue;
                        }
                        case 51: {
                            if (this.curChar == '[') {
                                this.jjAddStates(30, 31);
                                continue;
                            }
                            continue;
                        }
                        case 53: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(28, 29);
                                continue;
                            }
                            continue;
                        }
                        case 55: {
                            if (this.curChar == ']' && kind > 8) {
                                kind = 8;
                                continue;
                            }
                            continue;
                        }
                        case 56: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 55;
                                continue;
                            }
                            continue;
                        }
                        case 57: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 53;
                                continue;
                            }
                            continue;
                        }
                        case 58: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 57;
                                continue;
                            }
                            continue;
                        }
                        case 59: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 58;
                                continue;
                            }
                            continue;
                        }
                        case 60: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 59;
                                continue;
                            }
                            continue;
                        }
                        case 61: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 60;
                                continue;
                            }
                            continue;
                        }
                        case 62: {
                            if (this.curChar == 'd') {
                                this.jjstateSet[this.jjnewStateCnt++] = 61;
                                continue;
                            }
                            continue;
                        }
                        case 63: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 62;
                                continue;
                            }
                            continue;
                        }
                        case 64: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 63;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else {
                final int i2 = (this.curChar & '\u00ff') >> 6;
                final long l2 = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 33: {
                            if ((JVCParserTokenManager.jjbitVec0[i2] & l2) != 0x0L && kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            ++curPos;
            final int n = i = this.jjnewStateCnt;
            final int n2 = 65;
            final int jjnewStateCnt = startsAt;
            this.jjnewStateCnt = jjnewStateCnt;
            if (n == (startsAt = n2 - jjnewStateCnt)) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException e) {
                return curPos;
            }
        }
        return curPos;
    }
    
    private final int jjStopStringLiteralDfa_1(final int pos, final long active0) {
        switch (pos) {
            case 0: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 11;
                }
                return -1;
            }
            case 1: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 41;
                }
                return -1;
            }
            default: {
                return -1;
            }
        }
    }
    
    private final int jjStartNfa_1(final int pos, final long active0) {
        return this.jjMoveNfa_1(this.jjStopStringLiteralDfa_1(pos, active0), pos + 1);
    }
    
    private int jjMoveStringLiteralDfa0_1() {
        switch (this.curChar) {
            case '[': {
                return this.jjMoveStringLiteralDfa1_1(62L);
            }
            default: {
                return this.jjMoveNfa_1(0, 0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa1_1(final long active0) {
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_1(0, active0);
            return 1;
        }
        switch (this.curChar) {
            case '[': {
                if ((active0 & 0x2L) != 0x0L) {
                    this.jjmatchedKind = 1;
                    this.jjmatchedPos = 1;
                }
                return this.jjMoveStringLiteralDfa2_1(active0, 60L);
            }
            default: {
                return this.jjStartNfa_1(0, active0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa2_1(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_1(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_1(1, active0);
            return 2;
        }
        switch (this.curChar) {
            case '!': {
                if ((active0 & 0x20L) != 0x0L) {
                    return this.jjStopAtPos(2, 5);
                }
                break;
            }
            case '+': {
                if ((active0 & 0x10L) != 0x0L) {
                    return this.jjStopAtPos(2, 4);
                }
                break;
            }
            case '=': {
                if ((active0 & 0x4L) != 0x0L) {
                    this.jjmatchedKind = 2;
                    this.jjmatchedPos = 2;
                }
                return this.jjMoveStringLiteralDfa3_1(active0, 8L);
            }
        }
        return this.jjStartNfa_1(1, active0);
    }
    
    private int jjMoveStringLiteralDfa3_1(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_1(1, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_1(2, active0);
            return 3;
        }
        switch (this.curChar) {
            case '=': {
                if ((active0 & 0x8L) != 0x0L) {
                    return this.jjStopAtPos(3, 3);
                }
                break;
            }
        }
        return this.jjStartNfa_1(2, active0);
    }
    
    private int jjMoveNfa_1(final int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 41;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            if (++this.jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            if (this.curChar < '@') {
                final long l = 1L << this.curChar;
                do {
                    switch (this.jjstateSet[--i]) {
                        case 41: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                            }
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if ((0x2400L & l) != 0x0L && kind > 6) {
                                kind = 6;
                            }
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 1: {
                            if (this.curChar == '\n' && kind > 6) {
                                kind = 6;
                                continue;
                            }
                            continue;
                        }
                        case 2: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 4: {
                            if ((0x2400L & l) != 0x0L && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 5: {
                            if (this.curChar == '\n' && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 6: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 5;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 12: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 14: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 28: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                                continue;
                            }
                            continue;
                        }
                        case 30: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else if (this.curChar < '\u0080') {
                final long l = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 11: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                            }
                            if (this.curChar == '[') {
                                this.jjAddStates(6, 7);
                                continue;
                            }
                            continue;
                        }
                        case 41: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 3: {
                            if (this.curChar != ']') {
                                continue;
                            }
                            if (kind > 9) {
                                kind = 9;
                            }
                            this.jjAddStates(10, 11);
                            continue;
                        }
                        case 7: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 8: {
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 10: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            continue;
                        }
                        case 13: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 15: {
                            if (this.curChar == ']' && kind > 7) {
                                kind = 7;
                                continue;
                            }
                            continue;
                        }
                        case 16: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 15;
                                continue;
                            }
                            continue;
                        }
                        case 17: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 13;
                                continue;
                            }
                            continue;
                        }
                        case 18: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 17;
                                continue;
                            }
                            continue;
                        }
                        case 19: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 18;
                                continue;
                            }
                            continue;
                        }
                        case 20: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 19;
                                continue;
                            }
                            continue;
                        }
                        case 21: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 20;
                                continue;
                            }
                            continue;
                        }
                        case 22: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 21;
                                continue;
                            }
                            continue;
                        }
                        case 23: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 22;
                                continue;
                            }
                            continue;
                        }
                        case 24: {
                            if (this.curChar == 'g') {
                                this.jjstateSet[this.jjnewStateCnt++] = 23;
                                continue;
                            }
                            continue;
                        }
                        case 25: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 24;
                                continue;
                            }
                            continue;
                        }
                        case 26: {
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 27: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                                continue;
                            }
                            continue;
                        }
                        case 29: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        case 31: {
                            if (this.curChar == ']' && kind > 8) {
                                kind = 8;
                                continue;
                            }
                            continue;
                        }
                        case 32: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 31;
                                continue;
                            }
                            continue;
                        }
                        case 33: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 29;
                                continue;
                            }
                            continue;
                        }
                        case 34: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                                continue;
                            }
                            continue;
                        }
                        case 35: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 34;
                                continue;
                            }
                            continue;
                        }
                        case 36: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 35;
                                continue;
                            }
                            continue;
                        }
                        case 37: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 36;
                                continue;
                            }
                            continue;
                        }
                        case 38: {
                            if (this.curChar == 'd') {
                                this.jjstateSet[this.jjnewStateCnt++] = 37;
                                continue;
                            }
                            continue;
                        }
                        case 39: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 38;
                                continue;
                            }
                            continue;
                        }
                        case 40: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else {
                final int i2 = (this.curChar & '\u00ff') >> 6;
                final long l2 = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 9: {
                            if ((JVCParserTokenManager.jjbitVec0[i2] & l2) != 0x0L && kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            ++curPos;
            final int n = i = this.jjnewStateCnt;
            final int n2 = 41;
            final int jjnewStateCnt = startsAt;
            this.jjnewStateCnt = jjnewStateCnt;
            if (n == (startsAt = n2 - jjnewStateCnt)) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException e) {
                return curPos;
            }
        }
        return curPos;
    }
    
    private final int jjStopStringLiteralDfa_3(final int pos, final long active0) {
        switch (pos) {
            case 0: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 11;
                }
                return -1;
            }
            case 1: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 41;
                }
                return -1;
            }
            default: {
                return -1;
            }
        }
    }
    
    private final int jjStartNfa_3(final int pos, final long active0) {
        return this.jjMoveNfa_3(this.jjStopStringLiteralDfa_3(pos, active0), pos + 1);
    }
    
    private int jjMoveStringLiteralDfa0_3() {
        switch (this.curChar) {
            case '[': {
                return this.jjMoveStringLiteralDfa1_3(62L);
            }
            default: {
                return this.jjMoveNfa_3(0, 0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa1_3(final long active0) {
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_3(0, active0);
            return 1;
        }
        switch (this.curChar) {
            case '[': {
                if ((active0 & 0x2L) != 0x0L) {
                    this.jjmatchedKind = 1;
                    this.jjmatchedPos = 1;
                }
                return this.jjMoveStringLiteralDfa2_3(active0, 60L);
            }
            default: {
                return this.jjStartNfa_3(0, active0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa2_3(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_3(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_3(1, active0);
            return 2;
        }
        switch (this.curChar) {
            case '!': {
                if ((active0 & 0x20L) != 0x0L) {
                    return this.jjStopAtPos(2, 5);
                }
                break;
            }
            case '+': {
                if ((active0 & 0x10L) != 0x0L) {
                    return this.jjStopAtPos(2, 4);
                }
                break;
            }
            case '=': {
                if ((active0 & 0x4L) != 0x0L) {
                    this.jjmatchedKind = 2;
                    this.jjmatchedPos = 2;
                }
                return this.jjMoveStringLiteralDfa3_3(active0, 8L);
            }
        }
        return this.jjStartNfa_3(1, active0);
    }
    
    private int jjMoveStringLiteralDfa3_3(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_3(1, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_3(2, active0);
            return 3;
        }
        switch (this.curChar) {
            case '=': {
                if ((active0 & 0x8L) != 0x0L) {
                    return this.jjStopAtPos(3, 3);
                }
                break;
            }
        }
        return this.jjStartNfa_3(2, active0);
    }
    
    private int jjMoveNfa_3(final int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 41;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            if (++this.jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            if (this.curChar < '@') {
                final long l = 1L << this.curChar;
                do {
                    switch (this.jjstateSet[--i]) {
                        case 41: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                            }
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if ((0x2400L & l) != 0x0L && kind > 6) {
                                kind = 6;
                            }
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 1: {
                            if (this.curChar == '\n' && kind > 6) {
                                kind = 6;
                                continue;
                            }
                            continue;
                        }
                        case 2: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 4: {
                            if ((0x2400L & l) != 0x0L && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 5: {
                            if (this.curChar == '\n' && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 6: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 5;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 12: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 14: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 28: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                                continue;
                            }
                            continue;
                        }
                        case 30: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else if (this.curChar < '\u0080') {
                final long l = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 11: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                            }
                            if (this.curChar == '[') {
                                this.jjAddStates(6, 7);
                                continue;
                            }
                            continue;
                        }
                        case 41: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 3: {
                            if (this.curChar != ']') {
                                continue;
                            }
                            if (kind > 9) {
                                kind = 9;
                            }
                            this.jjAddStates(10, 11);
                            continue;
                        }
                        case 7: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 8: {
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 10: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            continue;
                        }
                        case 13: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 15: {
                            if (this.curChar == ']' && kind > 7) {
                                kind = 7;
                                continue;
                            }
                            continue;
                        }
                        case 16: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 15;
                                continue;
                            }
                            continue;
                        }
                        case 17: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 13;
                                continue;
                            }
                            continue;
                        }
                        case 18: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 17;
                                continue;
                            }
                            continue;
                        }
                        case 19: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 18;
                                continue;
                            }
                            continue;
                        }
                        case 20: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 19;
                                continue;
                            }
                            continue;
                        }
                        case 21: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 20;
                                continue;
                            }
                            continue;
                        }
                        case 22: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 21;
                                continue;
                            }
                            continue;
                        }
                        case 23: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 22;
                                continue;
                            }
                            continue;
                        }
                        case 24: {
                            if (this.curChar == 'g') {
                                this.jjstateSet[this.jjnewStateCnt++] = 23;
                                continue;
                            }
                            continue;
                        }
                        case 25: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 24;
                                continue;
                            }
                            continue;
                        }
                        case 26: {
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 27: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                                continue;
                            }
                            continue;
                        }
                        case 29: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        case 31: {
                            if (this.curChar == ']' && kind > 8) {
                                kind = 8;
                                continue;
                            }
                            continue;
                        }
                        case 32: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 31;
                                continue;
                            }
                            continue;
                        }
                        case 33: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 29;
                                continue;
                            }
                            continue;
                        }
                        case 34: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                                continue;
                            }
                            continue;
                        }
                        case 35: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 34;
                                continue;
                            }
                            continue;
                        }
                        case 36: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 35;
                                continue;
                            }
                            continue;
                        }
                        case 37: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 36;
                                continue;
                            }
                            continue;
                        }
                        case 38: {
                            if (this.curChar == 'd') {
                                this.jjstateSet[this.jjnewStateCnt++] = 37;
                                continue;
                            }
                            continue;
                        }
                        case 39: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 38;
                                continue;
                            }
                            continue;
                        }
                        case 40: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else {
                final int i2 = (this.curChar & '\u00ff') >> 6;
                final long l2 = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 9: {
                            if ((JVCParserTokenManager.jjbitVec0[i2] & l2) != 0x0L && kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            ++curPos;
            final int n = i = this.jjnewStateCnt;
            final int n2 = 41;
            final int jjnewStateCnt = startsAt;
            this.jjnewStateCnt = jjnewStateCnt;
            if (n == (startsAt = n2 - jjnewStateCnt)) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException e) {
                return curPos;
            }
        }
        return curPos;
    }
    
    private final int jjStopStringLiteralDfa_2(final int pos, final long active0) {
        switch (pos) {
            case 0: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 11;
                }
                return -1;
            }
            case 1: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 41;
                }
                return -1;
            }
            default: {
                return -1;
            }
        }
    }
    
    private final int jjStartNfa_2(final int pos, final long active0) {
        return this.jjMoveNfa_2(this.jjStopStringLiteralDfa_2(pos, active0), pos + 1);
    }
    
    private int jjMoveStringLiteralDfa0_2() {
        switch (this.curChar) {
            case '[': {
                return this.jjMoveStringLiteralDfa1_2(62L);
            }
            default: {
                return this.jjMoveNfa_2(0, 0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa1_2(final long active0) {
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_2(0, active0);
            return 1;
        }
        switch (this.curChar) {
            case '[': {
                if ((active0 & 0x2L) != 0x0L) {
                    this.jjmatchedKind = 1;
                    this.jjmatchedPos = 1;
                }
                return this.jjMoveStringLiteralDfa2_2(active0, 60L);
            }
            default: {
                return this.jjStartNfa_2(0, active0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa2_2(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_2(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_2(1, active0);
            return 2;
        }
        switch (this.curChar) {
            case '!': {
                if ((active0 & 0x20L) != 0x0L) {
                    return this.jjStopAtPos(2, 5);
                }
                break;
            }
            case '+': {
                if ((active0 & 0x10L) != 0x0L) {
                    return this.jjStopAtPos(2, 4);
                }
                break;
            }
            case '=': {
                if ((active0 & 0x4L) != 0x0L) {
                    this.jjmatchedKind = 2;
                    this.jjmatchedPos = 2;
                }
                return this.jjMoveStringLiteralDfa3_2(active0, 8L);
            }
        }
        return this.jjStartNfa_2(1, active0);
    }
    
    private int jjMoveStringLiteralDfa3_2(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_2(1, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_2(2, active0);
            return 3;
        }
        switch (this.curChar) {
            case '=': {
                if ((active0 & 0x8L) != 0x0L) {
                    return this.jjStopAtPos(3, 3);
                }
                break;
            }
        }
        return this.jjStartNfa_2(2, active0);
    }
    
    private int jjMoveNfa_2(final int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 41;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            if (++this.jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            if (this.curChar < '@') {
                final long l = 1L << this.curChar;
                do {
                    switch (this.jjstateSet[--i]) {
                        case 41: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                            }
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if ((0x2400L & l) != 0x0L && kind > 6) {
                                kind = 6;
                            }
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 1: {
                            if (this.curChar == '\n' && kind > 6) {
                                kind = 6;
                                continue;
                            }
                            continue;
                        }
                        case 2: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 4: {
                            if ((0x2400L & l) != 0x0L && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 5: {
                            if (this.curChar == '\n' && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 6: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 5;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 12: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 14: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 28: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                                continue;
                            }
                            continue;
                        }
                        case 30: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else if (this.curChar < '\u0080') {
                final long l = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 11: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                            }
                            if (this.curChar == '[') {
                                this.jjAddStates(6, 7);
                                continue;
                            }
                            continue;
                        }
                        case 41: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 3: {
                            if (this.curChar != ']') {
                                continue;
                            }
                            if (kind > 9) {
                                kind = 9;
                            }
                            this.jjAddStates(10, 11);
                            continue;
                        }
                        case 7: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 8: {
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 10: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            continue;
                        }
                        case 13: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 15: {
                            if (this.curChar == ']' && kind > 7) {
                                kind = 7;
                                continue;
                            }
                            continue;
                        }
                        case 16: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 15;
                                continue;
                            }
                            continue;
                        }
                        case 17: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 13;
                                continue;
                            }
                            continue;
                        }
                        case 18: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 17;
                                continue;
                            }
                            continue;
                        }
                        case 19: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 18;
                                continue;
                            }
                            continue;
                        }
                        case 20: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 19;
                                continue;
                            }
                            continue;
                        }
                        case 21: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 20;
                                continue;
                            }
                            continue;
                        }
                        case 22: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 21;
                                continue;
                            }
                            continue;
                        }
                        case 23: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 22;
                                continue;
                            }
                            continue;
                        }
                        case 24: {
                            if (this.curChar == 'g') {
                                this.jjstateSet[this.jjnewStateCnt++] = 23;
                                continue;
                            }
                            continue;
                        }
                        case 25: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 24;
                                continue;
                            }
                            continue;
                        }
                        case 26: {
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 27: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                                continue;
                            }
                            continue;
                        }
                        case 29: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        case 31: {
                            if (this.curChar == ']' && kind > 8) {
                                kind = 8;
                                continue;
                            }
                            continue;
                        }
                        case 32: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 31;
                                continue;
                            }
                            continue;
                        }
                        case 33: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 29;
                                continue;
                            }
                            continue;
                        }
                        case 34: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                                continue;
                            }
                            continue;
                        }
                        case 35: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 34;
                                continue;
                            }
                            continue;
                        }
                        case 36: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 35;
                                continue;
                            }
                            continue;
                        }
                        case 37: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 36;
                                continue;
                            }
                            continue;
                        }
                        case 38: {
                            if (this.curChar == 'd') {
                                this.jjstateSet[this.jjnewStateCnt++] = 37;
                                continue;
                            }
                            continue;
                        }
                        case 39: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 38;
                                continue;
                            }
                            continue;
                        }
                        case 40: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else {
                final int i2 = (this.curChar & '\u00ff') >> 6;
                final long l2 = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 9: {
                            if ((JVCParserTokenManager.jjbitVec0[i2] & l2) != 0x0L && kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            ++curPos;
            final int n = i = this.jjnewStateCnt;
            final int n2 = 41;
            final int jjnewStateCnt = startsAt;
            this.jjnewStateCnt = jjnewStateCnt;
            if (n == (startsAt = n2 - jjnewStateCnt)) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException e) {
                return curPos;
            }
        }
        return curPos;
    }
    
    private final int jjStopStringLiteralDfa_0(final int pos, final long active0) {
        switch (pos) {
            case 0: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 11;
                }
                return -1;
            }
            case 1: {
                if ((active0 & 0x3EL) != 0x0L) {
                    return 41;
                }
                return -1;
            }
            default: {
                return -1;
            }
        }
    }
    
    private final int jjStartNfa_0(final int pos, final long active0) {
        return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }
    
    private int jjMoveStringLiteralDfa0_0() {
        switch (this.curChar) {
            case '[': {
                return this.jjMoveStringLiteralDfa1_0(62L);
            }
            default: {
                return this.jjMoveNfa_0(0, 0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa1_0(final long active0) {
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch (this.curChar) {
            case '[': {
                if ((active0 & 0x2L) != 0x0L) {
                    this.jjmatchedKind = 1;
                    this.jjmatchedPos = 1;
                }
                return this.jjMoveStringLiteralDfa2_0(active0, 60L);
            }
            default: {
                return this.jjStartNfa_0(0, active0);
            }
        }
    }
    
    private int jjMoveStringLiteralDfa2_0(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_0(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_0(1, active0);
            return 2;
        }
        switch (this.curChar) {
            case '!': {
                if ((active0 & 0x20L) != 0x0L) {
                    return this.jjStopAtPos(2, 5);
                }
                break;
            }
            case '+': {
                if ((active0 & 0x10L) != 0x0L) {
                    return this.jjStopAtPos(2, 4);
                }
                break;
            }
            case '=': {
                if ((active0 & 0x4L) != 0x0L) {
                    this.jjmatchedKind = 2;
                    this.jjmatchedPos = 2;
                }
                return this.jjMoveStringLiteralDfa3_0(active0, 8L);
            }
        }
        return this.jjStartNfa_0(1, active0);
    }
    
    private int jjMoveStringLiteralDfa3_0(final long old0, long active0) {
        if ((active0 &= old0) == 0x0L) {
            return this.jjStartNfa_0(1, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch (IOException e) {
            this.jjStopStringLiteralDfa_0(2, active0);
            return 3;
        }
        switch (this.curChar) {
            case '=': {
                if ((active0 & 0x8L) != 0x0L) {
                    return this.jjStopAtPos(3, 3);
                }
                break;
            }
        }
        return this.jjStartNfa_0(2, active0);
    }
    
    private int jjMoveNfa_0(final int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 41;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            if (++this.jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            if (this.curChar < '@') {
                final long l = 1L << this.curChar;
                do {
                    switch (this.jjstateSet[--i]) {
                        case 41: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                            }
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if ((0x2400L & l) != 0x0L && kind > 6) {
                                kind = 6;
                            }
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 1: {
                            if (this.curChar == '\n' && kind > 6) {
                                kind = 6;
                                continue;
                            }
                            continue;
                        }
                        case 2: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                                continue;
                            }
                            continue;
                        }
                        case 4: {
                            if ((0x2400L & l) != 0x0L && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 5: {
                            if (this.curChar == '\n' && kind > 9) {
                                kind = 9;
                                continue;
                            }
                            continue;
                        }
                        case 6: {
                            if (this.curChar == '\r') {
                                this.jjstateSet[this.jjnewStateCnt++] = 5;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 12: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(12, 26);
                                continue;
                            }
                            continue;
                        }
                        case 14: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 28: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjCheckNAddTwoStates(28, 40);
                                continue;
                            }
                            continue;
                        }
                        case 30: {
                            if ((0x100002600L & l) != 0x0L) {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else if (this.curChar < '\u0080') {
                final long l = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 11: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                            }
                            if (this.curChar == '[') {
                                this.jjAddStates(6, 7);
                                continue;
                            }
                            continue;
                        }
                        case 41: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 0: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 3: {
                            if (this.curChar != ']') {
                                continue;
                            }
                            if (kind > 9) {
                                kind = 9;
                            }
                            this.jjAddStates(10, 11);
                            continue;
                        }
                        case 7: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 3;
                                continue;
                            }
                            continue;
                        }
                        case 8: {
                            if (this.curChar == '\\') {
                                this.jjstateSet[this.jjnewStateCnt++] = 9;
                                continue;
                            }
                            continue;
                        }
                        case 9: {
                            if (kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        case 10: {
                            if (this.curChar == '[') {
                                this.jjAddStates(8, 9);
                                continue;
                            }
                            continue;
                        }
                        case 13: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(0, 1);
                                continue;
                            }
                            continue;
                        }
                        case 15: {
                            if (this.curChar == ']' && kind > 7) {
                                kind = 7;
                                continue;
                            }
                            continue;
                        }
                        case 16: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 15;
                                continue;
                            }
                            continue;
                        }
                        case 17: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 13;
                                continue;
                            }
                            continue;
                        }
                        case 18: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 17;
                                continue;
                            }
                            continue;
                        }
                        case 19: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 18;
                                continue;
                            }
                            continue;
                        }
                        case 20: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 19;
                                continue;
                            }
                            continue;
                        }
                        case 21: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 20;
                                continue;
                            }
                            continue;
                        }
                        case 22: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 21;
                                continue;
                            }
                            continue;
                        }
                        case 23: {
                            if (this.curChar == 'i') {
                                this.jjstateSet[this.jjnewStateCnt++] = 22;
                                continue;
                            }
                            continue;
                        }
                        case 24: {
                            if (this.curChar == 'g') {
                                this.jjstateSet[this.jjnewStateCnt++] = 23;
                                continue;
                            }
                            continue;
                        }
                        case 25: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 24;
                                continue;
                            }
                            continue;
                        }
                        case 26: {
                            if (this.curChar == 'b') {
                                this.jjstateSet[this.jjnewStateCnt++] = 25;
                                continue;
                            }
                            continue;
                        }
                        case 27: {
                            if (this.curChar == '[') {
                                this.jjAddStates(4, 5);
                                continue;
                            }
                            continue;
                        }
                        case 29: {
                            if (this.curChar == 'e') {
                                this.jjAddStates(2, 3);
                                continue;
                            }
                            continue;
                        }
                        case 31: {
                            if (this.curChar == ']' && kind > 8) {
                                kind = 8;
                                continue;
                            }
                            continue;
                        }
                        case 32: {
                            if (this.curChar == ']') {
                                this.jjstateSet[this.jjnewStateCnt++] = 31;
                                continue;
                            }
                            continue;
                        }
                        case 33: {
                            if (this.curChar == 'h') {
                                this.jjstateSet[this.jjnewStateCnt++] = 29;
                                continue;
                            }
                            continue;
                        }
                        case 34: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                                continue;
                            }
                            continue;
                        }
                        case 35: {
                            if (this.curChar == 'a') {
                                this.jjstateSet[this.jjnewStateCnt++] = 34;
                                continue;
                            }
                            continue;
                        }
                        case 36: {
                            if (this.curChar == 'c') {
                                this.jjstateSet[this.jjnewStateCnt++] = 35;
                                continue;
                            }
                            continue;
                        }
                        case 37: {
                            if (this.curChar == '_') {
                                this.jjstateSet[this.jjnewStateCnt++] = 36;
                                continue;
                            }
                            continue;
                        }
                        case 38: {
                            if (this.curChar == 'd') {
                                this.jjstateSet[this.jjnewStateCnt++] = 37;
                                continue;
                            }
                            continue;
                        }
                        case 39: {
                            if (this.curChar == 'n') {
                                this.jjstateSet[this.jjnewStateCnt++] = 38;
                                continue;
                            }
                            continue;
                        }
                        case 40: {
                            if (this.curChar == 'e') {
                                this.jjstateSet[this.jjnewStateCnt++] = 39;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            else {
                final int i2 = (this.curChar & '\u00ff') >> 6;
                final long l2 = 1L << (this.curChar & '?');
                do {
                    switch (this.jjstateSet[--i]) {
                        case 9: {
                            if ((JVCParserTokenManager.jjbitVec0[i2] & l2) != 0x0L && kind > 15) {
                                kind = 15;
                                continue;
                            }
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            ++curPos;
            final int n = i = this.jjnewStateCnt;
            final int n2 = 41;
            final int jjnewStateCnt = startsAt;
            this.jjnewStateCnt = jjnewStateCnt;
            if (n == (startsAt = n2 - jjnewStateCnt)) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException e) {
                return curPos;
            }
        }
        return curPos;
    }
    
    public JVCParserTokenManager(final SimpleCharStream stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[65];
        this.jjstateSet = new int[130];
        this.curLexState = 5;
        this.defaultLexState = 5;
        this.input_stream = stream;
    }
    
    public JVCParserTokenManager(final SimpleCharStream stream, final int lexState) {
        this(stream);
        this.SwitchTo(lexState);
    }
    
    public void ReInit(final SimpleCharStream stream) {
        final boolean b = false;
        this.jjnewStateCnt = (b ? 1 : 0);
        this.jjmatchedPos = (b ? 1 : 0);
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        this.ReInitRounds();
    }
    
    private void ReInitRounds() {
        this.jjround = -2147483647;
        int i = 65;
        while (i-- > 0) {
            this.jjrounds[i] = Integer.MIN_VALUE;
        }
    }
    
    public void ReInit(final SimpleCharStream stream, final int lexState) {
        this.ReInit(stream);
        this.SwitchTo(lexState);
    }
    
    public void SwitchTo(final int lexState) {
        if (lexState >= 6 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }
    
    protected Token jjFillToken() {
        final String im = JVCParserTokenManager.jjstrLiteralImages[this.jjmatchedKind];
        final String curTokenImage = (im == null) ? this.input_stream.GetImage() : im;
        final int beginLine = this.input_stream.getBeginLine();
        final int beginColumn = this.input_stream.getBeginColumn();
        final int endLine = this.input_stream.getEndLine();
        final int endColumn = this.input_stream.getEndColumn();
        final Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;
        return t;
    }
    
    public Token getNextToken() {
        Token specialToken = null;
        int curPos = 0;
        while (true) {
            try {
                this.curChar = this.input_stream.BeginToken();
            }
            catch (IOException e) {
                this.jjmatchedKind = 0;
                final Token matchedToken = this.jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }
            switch (this.curLexState) {
                case 0: {
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    curPos = this.jjMoveStringLiteralDfa0_0();
                    if (this.jjmatchedPos == 0 && this.jjmatchedKind > 16) {
                        this.jjmatchedKind = 16;
                        break;
                    }
                    break;
                }
                case 1: {
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    curPos = this.jjMoveStringLiteralDfa0_1();
                    if (this.jjmatchedPos == 0 && this.jjmatchedKind > 16) {
                        this.jjmatchedKind = 16;
                        break;
                    }
                    break;
                }
                case 2: {
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    curPos = this.jjMoveStringLiteralDfa0_2();
                    if (this.jjmatchedPos == 0 && this.jjmatchedKind > 16) {
                        this.jjmatchedKind = 16;
                        break;
                    }
                    break;
                }
                case 3: {
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    curPos = this.jjMoveStringLiteralDfa0_3();
                    if (this.jjmatchedPos == 0 && this.jjmatchedKind > 16) {
                        this.jjmatchedKind = 16;
                        break;
                    }
                    break;
                }
                case 4: {
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    curPos = this.jjMoveStringLiteralDfa0_4();
                    if (this.jjmatchedPos == 0 && this.jjmatchedKind > 16) {
                        this.jjmatchedKind = 16;
                        break;
                    }
                    break;
                }
                case 5: {
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    curPos = this.jjMoveStringLiteralDfa0_5();
                    if (this.jjmatchedPos == 0 && this.jjmatchedKind > 16) {
                        this.jjmatchedKind = 16;
                        break;
                    }
                    break;
                }
            }
            if (this.jjmatchedKind == Integer.MAX_VALUE) {
                int error_line = this.input_stream.getEndLine();
                int error_column = this.input_stream.getEndColumn();
                String error_after = null;
                boolean EOFSeen = false;
                try {
                    this.input_stream.readChar();
                    this.input_stream.backup(1);
                }
                catch (IOException e2) {
                    EOFSeen = true;
                    error_after = ((curPos <= 1) ? "" : this.input_stream.GetImage());
                    if (this.curChar == '\n' || this.curChar == '\r') {
                        ++error_line;
                        error_column = 0;
                    }
                    else {
                        ++error_column;
                    }
                }
                if (!EOFSeen) {
                    this.input_stream.backup(1);
                    error_after = ((curPos <= 1) ? "" : this.input_stream.GetImage());
                }
                throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
            }
            if (this.jjmatchedPos + 1 < curPos) {
                this.input_stream.backup(curPos - this.jjmatchedPos - 1);
            }
            if ((JVCParserTokenManager.jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0x0L) {
                final Token matchedToken = this.jjFillToken();
                matchedToken.specialToken = specialToken;
                if (JVCParserTokenManager.jjnewLexState[this.jjmatchedKind] != -1) {
                    this.curLexState = JVCParserTokenManager.jjnewLexState[this.jjmatchedKind];
                }
                return matchedToken;
            }
            if ((JVCParserTokenManager.jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0x0L) {
                final Token matchedToken = this.jjFillToken();
                if (specialToken == null) {
                    specialToken = matchedToken;
                }
                else {
                    matchedToken.specialToken = specialToken;
                    final Token token = specialToken;
                    final Token next = matchedToken;
                    token.next = next;
                    specialToken = next;
                }
            }
            if (JVCParserTokenManager.jjnewLexState[this.jjmatchedKind] == -1) {
                continue;
            }
            this.curLexState = JVCParserTokenManager.jjnewLexState[this.jjmatchedKind];
        }
    }
    
    private void jjCheckNAdd(final int state) {
        if (this.jjrounds[state] != this.jjround) {
            this.jjstateSet[this.jjnewStateCnt++] = state;
            this.jjrounds[state] = this.jjround;
        }
    }
    
    private void jjAddStates(int start, final int end) {
        do {
            this.jjstateSet[this.jjnewStateCnt++] = JVCParserTokenManager.jjnextStates[start];
        } while (start++ != end);
    }
    
    private void jjCheckNAddTwoStates(final int state1, final int state2) {
        this.jjCheckNAdd(state1);
        this.jjCheckNAdd(state2);
    }
    
    private void jjCheckNAddStates(int start, final int end) {
        do {
            this.jjCheckNAdd(JVCParserTokenManager.jjnextStates[start]);
        } while (start++ != end);
    }
    
    static {
        jjbitVec0 = new long[] { 0L, 0L, -1L, -1L };
        jjnextStates = new int[] { 14, 16, 30, 32, 28, 40, 12, 26, 11, 27, 4, 6, 10, 26, 12, 13, 14, 15, 21, 19, 20, 15, 17, 21, 19, 20, 38, 40, 54, 56, 52, 64, 36, 50, 35, 51 };
        jjstrLiteralImages = new String[] { "", "[[", "[[=", "[[==", "[[+", "[[!", null, null, null, null, null, null, null, null, null, null, null };
        lexStateNames = new String[] { "IN_COMMENT", "IN_LOAD", "IN_EXPR2", "IN_EXPR", "IN_CODE", "DEFAULT" };
        jjnewLexState = new int[] { -1, 4, 3, 2, 1, 0, -1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1 };
        jjtoToken = new long[] { 67583L };
        jjtoSkip = new long[] { 32768L };
        jjtoSpecial = new long[] { 32768L };
    }
}
