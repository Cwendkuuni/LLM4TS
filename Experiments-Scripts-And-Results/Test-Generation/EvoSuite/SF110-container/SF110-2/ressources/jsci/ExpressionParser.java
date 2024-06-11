package JSci.maths.symbolic;

import JSci.maths.groups.*;
import JSci.maths.fields.*;
import JSci.maths.*;
import java.io.*;
import java.util.*;

public class ExpressionParser implements ExpressionParserConstants
{
    private static ExpressionParser parser;
    private static Map variables;
    private static boolean jj_initialized_once;
    public static ExpressionParserTokenManager token_source;
    static SimpleCharStream jj_input_stream;
    public static Token token;
    public static Token jj_nt;
    private static int jj_ntk;
    private static Token jj_scanpos;
    private static Token jj_lastpos;
    private static int jj_la;
    public static boolean lookingAhead;
    private static boolean jj_semLA;
    private static int jj_gen;
    private static final int[] jj_la1;
    private static int[] jj_la1_0;
    private static final JJCalls[] jj_2_rtns;
    private static boolean jj_rescan;
    private static int jj_gc;
    private static final LookaheadSuccess jj_ls;
    private static Vector jj_expentries;
    private static int[] jj_expentry;
    private static int jj_kind;
    private static int[] jj_lasttokens;
    private static int jj_endpos;
    
    public static synchronized Expression parse(final InputStream is, final Map vars) throws ParseException {
        ExpressionParser.variables = vars;
        if (ExpressionParser.variables == null) {
            ExpressionParser.variables = new Hashtable();
        }
        if (ExpressionParser.parser == null) {
            ExpressionParser.parser = new ExpressionParser(is);
        }
        else {
            final ExpressionParser parser = ExpressionParser.parser;
            ReInit(is);
        }
        final ExpressionParser parser2 = ExpressionParser.parser;
        return Input();
    }
    
    public static Expression parse(final String s, final Map vars) throws ParseException {
        final InputStream i = new ByteArrayInputStream(s.getBytes());
        return parse(i, vars);
    }
    
    public static Expression parse(final String s) throws ParseException {
        final InputStream i = new ByteArrayInputStream(s.getBytes());
        return parse(i, null);
    }
    
    public static Expression parse(final InputStream is) throws ParseException {
        return parse(is, null);
    }
    
    public static final Expression Input() throws ParseException {
        Expression r = null;
        if (jj_2_1(60)) {
            r = expressionP0();
        }
        while (jj_2_2(60)) {
            if (jj_2_3(60)) {
                jj_consume_token(1);
            }
            else {
                if (!jj_2_4(60)) {
                    jj_consume_token(-1);
                    throw new ParseException();
                }
                jj_consume_token(2);
            }
        }
        jj_consume_token(0);
        return r;
    }
    
    public static final Expression expressionP0() throws ParseException {
        if (jj_2_5(60)) {
            final Expression[] arg = expressionP0l();
            if (arg.length == 1) {
                return arg[0];
            }
            return new Sum(arg[0], arg[1]);
        }
        else {
            if (jj_2_6(60)) {
                final Expression argb = expressionP1();
                return argb;
            }
            jj_consume_token(-1);
            throw new ParseException();
        }
    }
    
    public static final Expression[] expressionP0l() throws ParseException {
        Expression arga = null;
        Expression[] argb = null;
        if (jj_2_7(60)) {
            arga = expressionP1();
            jj_consume_token(3);
            argb = expressionP0l();
            if (argb.length == 2) {
                return new Expression[] { arga, new Sum(argb[0], argb[1]) };
            }
            return new Expression[] { arga, argb[0] };
        }
        else if (jj_2_8(60)) {
            arga = expressionP1();
            jj_consume_token(4);
            argb = expressionP0l();
            final Expression b0neg = Expression.negative(argb[0]);
            if (argb.length == 2) {
                return new Expression[] { arga, new Sum(b0neg, argb[1]) };
            }
            return new Expression[] { arga, b0neg };
        }
        else {
            if (jj_2_9(60)) {
                arga = expressionP1();
                return new Expression[] { arga };
            }
            jj_consume_token(-1);
            throw new ParseException();
        }
    }
    
    public static final Expression expressionP1() throws ParseException {
        if (jj_2_10(60)) {
            final Expression[] arg = expressionP1l();
            if (arg.length == 1) {
                return arg[0];
            }
            return new Product(arg[0], arg[1]);
        }
        else {
            if (jj_2_11(60)) {
                final Expression argb = expressionP2();
                return argb;
            }
            jj_consume_token(-1);
            throw new ParseException();
        }
    }
    
    public static final Expression[] expressionP1l() throws ParseException {
        Expression arga = null;
        Expression[] argb = null;
        if (jj_2_12(60)) {
            arga = expressionP2();
            jj_consume_token(5);
            argb = expressionP1l();
            if (argb.length == 2) {
                return new Expression[] { arga, new Product(argb[0], argb[1]) };
            }
            return new Expression[] { arga, argb[0] };
        }
        else if (jj_2_13(60)) {
            arga = expressionP2();
            jj_consume_token(6);
            argb = expressionP1l();
            final Expression b0inv = Expression.inverse(argb[0]);
            if (argb.length == 2) {
                return new Expression[] { arga, new Product(b0inv, argb[1]) };
            }
            return new Expression[] { arga, b0inv };
        }
        else {
            if (jj_2_14(60)) {
                arga = expressionP2();
                return new Expression[] { arga };
            }
            jj_consume_token(-1);
            throw new ParseException();
        }
    }
    
    public static final Expression expressionP2() throws ParseException {
        Expression arg = null;
        if (jj_2_15(60)) {
            jj_consume_token(4);
            arg = expressionP3();
            return Expression.negative(arg);
        }
        if (jj_2_16(60)) {
            jj_consume_token(3);
            arg = expressionP3();
            return arg;
        }
        if (jj_2_17(60)) {
            arg = expressionP3();
            return arg;
        }
        jj_consume_token(-1);
        throw new ParseException();
    }
    
    public static final Expression expressionP3() throws ParseException {
        Expression arga = null;
        if (jj_2_18(60)) {
            arga = expressionP4();
            jj_consume_token(7);
            final Token f = jj_consume_token(17);
            return new Power(arga, Integer.parseInt(f.toString()));
        }
        if (jj_2_19(60)) {
            arga = expressionP4();
            return arga;
        }
        jj_consume_token(-1);
        throw new ParseException();
    }
    
    public static final Expression expressionP4() throws ParseException {
        Expression arg = null;
        if (jj_2_20(60)) {
            final Token f = jj_consume_token(18);
            return new Constant(new MathDouble(Double.parseDouble(f.toString())));
        }
        if (jj_2_21(60)) {
            final Token f = jj_consume_token(17);
            return new Constant(new MathDouble(Integer.parseInt(f.toString())));
        }
        if (jj_2_22(60)) {
            final Token f = jj_consume_token(14);
            Variable v = ExpressionParser.variables.get(f.toString());
            if (v == null) {
                v = new Variable(f.toString(), RealField.getInstance());
            }
            ExpressionParser.variables.put(f.toString(), v);
            return v;
        }
        if (jj_2_23(60)) {
            final Token f = jj_consume_token(12);
            return new Constant(RealField.PI);
        }
        if (jj_2_24(60)) {
            final Token f = jj_consume_token(13);
            return new Constant(Complex.I);
        }
        if (jj_2_25(60)) {
            final Token f = jj_consume_token(11);
            jj_consume_token(8);
            arg = expressionP0();
            jj_consume_token(9);
            return new Function(f.toString(), arg);
        }
        if (jj_2_26(60)) {
            jj_consume_token(8);
            arg = expressionP0();
            jj_consume_token(9);
            return arg;
        }
        jj_consume_token(-1);
        throw new ParseException();
    }
    
    private static final boolean jj_2_1(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_1();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(0, xla);
        }
    }
    
    private static final boolean jj_2_2(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_2();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(1, xla);
        }
    }
    
    private static final boolean jj_2_3(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_3();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(2, xla);
        }
    }
    
    private static final boolean jj_2_4(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_4();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(3, xla);
        }
    }
    
    private static final boolean jj_2_5(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_5();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(4, xla);
        }
    }
    
    private static final boolean jj_2_6(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_6();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(5, xla);
        }
    }
    
    private static final boolean jj_2_7(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_7();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(6, xla);
        }
    }
    
    private static final boolean jj_2_8(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_8();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(7, xla);
        }
    }
    
    private static final boolean jj_2_9(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_9();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(8, xla);
        }
    }
    
    private static final boolean jj_2_10(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_10();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(9, xla);
        }
    }
    
    private static final boolean jj_2_11(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_11();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(10, xla);
        }
    }
    
    private static final boolean jj_2_12(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_12();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(11, xla);
        }
    }
    
    private static final boolean jj_2_13(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_13();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(12, xla);
        }
    }
    
    private static final boolean jj_2_14(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_14();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(13, xla);
        }
    }
    
    private static final boolean jj_2_15(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_15();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(14, xla);
        }
    }
    
    private static final boolean jj_2_16(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_16();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(15, xla);
        }
    }
    
    private static final boolean jj_2_17(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_17();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(16, xla);
        }
    }
    
    private static final boolean jj_2_18(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_18();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(17, xla);
        }
    }
    
    private static final boolean jj_2_19(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_19();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(18, xla);
        }
    }
    
    private static final boolean jj_2_20(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_20();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(19, xla);
        }
    }
    
    private static final boolean jj_2_21(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_21();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(20, xla);
        }
    }
    
    private static final boolean jj_2_22(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_22();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(21, xla);
        }
    }
    
    private static final boolean jj_2_23(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_23();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(22, xla);
        }
    }
    
    private static final boolean jj_2_24(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_24();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(23, xla);
        }
    }
    
    private static final boolean jj_2_25(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_25();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(24, xla);
        }
    }
    
    private static final boolean jj_2_26(final int xla) {
        ExpressionParser.jj_la = xla;
        ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.token);
        try {
            return !jj_3_26();
        }
        catch (LookaheadSuccess ls) {
            return true;
        }
        finally {
            jj_save(25, xla);
        }
    }
    
    private static final boolean jj_3_11() {
        return jj_3R_6();
    }
    
    private static final boolean jj_3R_4() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_10()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_11()) {
                return true;
            }
        }
        return false;
    }
    
    private static final boolean jj_3_10() {
        return jj_3R_5();
    }
    
    private static final boolean jj_3_19() {
        return jj_3R_8();
    }
    
    private static final boolean jj_3R_7() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_18()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_19()) {
                return true;
            }
        }
        return false;
    }
    
    private static final boolean jj_3_18() {
        return jj_3R_8() || jj_scan_token(7) || jj_scan_token(17);
    }
    
    private static final boolean jj_3_9() {
        return jj_3R_4();
    }
    
    private static final boolean jj_3_4() {
        return jj_scan_token(2);
    }
    
    private static final boolean jj_3_17() {
        return jj_3R_7();
    }
    
    private static final boolean jj_3_8() {
        return jj_3R_4() || jj_scan_token(4) || jj_3R_3();
    }
    
    private static final boolean jj_3_16() {
        return jj_scan_token(3) || jj_3R_7();
    }
    
    private static final boolean jj_3_3() {
        return jj_scan_token(1);
    }
    
    private static final boolean jj_3_2() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_3()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_4()) {
                return true;
            }
        }
        return false;
    }
    
    private static final boolean jj_3R_6() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_15()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_16()) {
                ExpressionParser.jj_scanpos = xsp;
                if (jj_3_17()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static final boolean jj_3_15() {
        return jj_scan_token(4) || jj_3R_7();
    }
    
    private static final boolean jj_3R_3() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_7()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_8()) {
                ExpressionParser.jj_scanpos = xsp;
                if (jj_3_9()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static final boolean jj_3_26() {
        return jj_scan_token(8) || jj_3R_2() || jj_scan_token(9);
    }
    
    private static final boolean jj_3_7() {
        return jj_3R_4() || jj_scan_token(3) || jj_3R_3();
    }
    
    private static final boolean jj_3_25() {
        return jj_scan_token(11) || jj_scan_token(8) || jj_3R_2() || jj_scan_token(9);
    }
    
    private static final boolean jj_3_24() {
        return jj_scan_token(13);
    }
    
    private static final boolean jj_3_14() {
        return jj_3R_6();
    }
    
    private static final boolean jj_3_6() {
        return jj_3R_4();
    }
    
    private static final boolean jj_3_23() {
        return jj_scan_token(12);
    }
    
    private static final boolean jj_3R_2() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_5()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_6()) {
                return true;
            }
        }
        return false;
    }
    
    private static final boolean jj_3_5() {
        return jj_3R_3();
    }
    
    private static final boolean jj_3_13() {
        return jj_3R_6() || jj_scan_token(6) || jj_3R_5();
    }
    
    private static final boolean jj_3R_5() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_12()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_13()) {
                ExpressionParser.jj_scanpos = xsp;
                if (jj_3_14()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static final boolean jj_3_22() {
        return jj_scan_token(14);
    }
    
    private static final boolean jj_3_1() {
        return jj_3R_2();
    }
    
    private static final boolean jj_3_12() {
        return jj_3R_6() || jj_scan_token(5) || jj_3R_5();
    }
    
    private static final boolean jj_3_21() {
        return jj_scan_token(17);
    }
    
    private static final boolean jj_3R_8() {
        final Token xsp = ExpressionParser.jj_scanpos;
        if (jj_3_20()) {
            ExpressionParser.jj_scanpos = xsp;
            if (jj_3_21()) {
                ExpressionParser.jj_scanpos = xsp;
                if (jj_3_22()) {
                    ExpressionParser.jj_scanpos = xsp;
                    if (jj_3_23()) {
                        ExpressionParser.jj_scanpos = xsp;
                        if (jj_3_24()) {
                            ExpressionParser.jj_scanpos = xsp;
                            if (jj_3_25()) {
                                ExpressionParser.jj_scanpos = xsp;
                                if (jj_3_26()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private static final boolean jj_3_20() {
        return jj_scan_token(18);
    }
    
    private static void jj_la1_0() {
        ExpressionParser.jj_la1_0 = new int[0];
    }
    
    public ExpressionParser(final InputStream stream) {
        if (ExpressionParser.jj_initialized_once) {
            System.out.println("ERROR: Second call to constructor of static parser.  You must");
            System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        ExpressionParser.jj_initialized_once = true;
        ExpressionParser.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        ExpressionParser.token_source = new ExpressionParserTokenManager(ExpressionParser.jj_input_stream);
        ExpressionParser.token = new Token();
        ExpressionParser.jj_ntk = -1;
        ExpressionParser.jj_gen = 0;
        for (int i = 0; i < 0; ++i) {
            ExpressionParser.jj_la1[i] = -1;
        }
        for (int i = 0; i < ExpressionParser.jj_2_rtns.length; ++i) {
            ExpressionParser.jj_2_rtns[i] = new JJCalls();
        }
    }
    
    public static void ReInit(final InputStream stream) {
        ExpressionParser.jj_input_stream.ReInit(stream, 1, 1);
        final ExpressionParserTokenManager token_source = ExpressionParser.token_source;
        ExpressionParserTokenManager.ReInit(ExpressionParser.jj_input_stream);
        ExpressionParser.token = new Token();
        ExpressionParser.jj_ntk = -1;
        ExpressionParser.jj_gen = 0;
        for (int i = 0; i < 0; ++i) {
            ExpressionParser.jj_la1[i] = -1;
        }
        for (int i = 0; i < ExpressionParser.jj_2_rtns.length; ++i) {
            ExpressionParser.jj_2_rtns[i] = new JJCalls();
        }
    }
    
    public ExpressionParser(final Reader stream) {
        if (ExpressionParser.jj_initialized_once) {
            System.out.println("ERROR: Second call to constructor of static parser.  You must");
            System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        ExpressionParser.jj_initialized_once = true;
        ExpressionParser.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        ExpressionParser.token_source = new ExpressionParserTokenManager(ExpressionParser.jj_input_stream);
        ExpressionParser.token = new Token();
        ExpressionParser.jj_ntk = -1;
        ExpressionParser.jj_gen = 0;
        for (int i = 0; i < 0; ++i) {
            ExpressionParser.jj_la1[i] = -1;
        }
        for (int i = 0; i < ExpressionParser.jj_2_rtns.length; ++i) {
            ExpressionParser.jj_2_rtns[i] = new JJCalls();
        }
    }
    
    public static void ReInit(final Reader stream) {
        ExpressionParser.jj_input_stream.ReInit(stream, 1, 1);
        final ExpressionParserTokenManager token_source = ExpressionParser.token_source;
        ExpressionParserTokenManager.ReInit(ExpressionParser.jj_input_stream);
        ExpressionParser.token = new Token();
        ExpressionParser.jj_ntk = -1;
        ExpressionParser.jj_gen = 0;
        for (int i = 0; i < 0; ++i) {
            ExpressionParser.jj_la1[i] = -1;
        }
        for (int i = 0; i < ExpressionParser.jj_2_rtns.length; ++i) {
            ExpressionParser.jj_2_rtns[i] = new JJCalls();
        }
    }
    
    public ExpressionParser(final ExpressionParserTokenManager tm) {
        if (ExpressionParser.jj_initialized_once) {
            System.out.println("ERROR: Second call to constructor of static parser.  You must");
            System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        ExpressionParser.jj_initialized_once = true;
        ExpressionParser.token_source = tm;
        ExpressionParser.token = new Token();
        ExpressionParser.jj_ntk = -1;
        ExpressionParser.jj_gen = 0;
        for (int i = 0; i < 0; ++i) {
            ExpressionParser.jj_la1[i] = -1;
        }
        for (int i = 0; i < ExpressionParser.jj_2_rtns.length; ++i) {
            ExpressionParser.jj_2_rtns[i] = new JJCalls();
        }
    }
    
    public void ReInit(final ExpressionParserTokenManager tm) {
        ExpressionParser.token_source = tm;
        ExpressionParser.token = new Token();
        ExpressionParser.jj_ntk = -1;
        ExpressionParser.jj_gen = 0;
        for (int i = 0; i < 0; ++i) {
            ExpressionParser.jj_la1[i] = -1;
        }
        for (int i = 0; i < ExpressionParser.jj_2_rtns.length; ++i) {
            ExpressionParser.jj_2_rtns[i] = new JJCalls();
        }
    }
    
    private static final Token jj_consume_token(final int kind) throws ParseException {
        final Token oldToken;
        if ((oldToken = ExpressionParser.token).next != null) {
            ExpressionParser.token = ExpressionParser.token.next;
        }
        else {
            final Token token = ExpressionParser.token;
            final ExpressionParserTokenManager token_source = ExpressionParser.token_source;
            final Token nextToken = ExpressionParserTokenManager.getNextToken();
            token.next = nextToken;
            ExpressionParser.token = nextToken;
        }
        ExpressionParser.jj_ntk = -1;
        if (ExpressionParser.token.kind == kind) {
            ++ExpressionParser.jj_gen;
            if (++ExpressionParser.jj_gc > 100) {
                ExpressionParser.jj_gc = 0;
                for (int i = 0; i < ExpressionParser.jj_2_rtns.length; ++i) {
                    for (JJCalls c = ExpressionParser.jj_2_rtns[i]; c != null; c = c.next) {
                        if (c.gen < ExpressionParser.jj_gen) {
                            c.first = null;
                        }
                    }
                }
            }
            return ExpressionParser.token;
        }
        ExpressionParser.token = oldToken;
        ExpressionParser.jj_kind = kind;
        throw generateParseException();
    }
    
    private static final boolean jj_scan_token(final int kind) {
        if (ExpressionParser.jj_scanpos == ExpressionParser.jj_lastpos) {
            --ExpressionParser.jj_la;
            if (ExpressionParser.jj_scanpos.next == null) {
                final Token jj_scanpos = ExpressionParser.jj_scanpos;
                final ExpressionParserTokenManager token_source = ExpressionParser.token_source;
                final Token nextToken = ExpressionParserTokenManager.getNextToken();
                jj_scanpos.next = nextToken;
                ExpressionParser.jj_scanpos = nextToken;
                ExpressionParser.jj_lastpos = nextToken;
            }
            else {
                ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = ExpressionParser.jj_scanpos.next);
            }
        }
        else {
            ExpressionParser.jj_scanpos = ExpressionParser.jj_scanpos.next;
        }
        if (ExpressionParser.jj_rescan) {
            int i = 0;
            Token tok;
            for (tok = ExpressionParser.token; tok != null && tok != ExpressionParser.jj_scanpos; tok = tok.next) {
                ++i;
            }
            if (tok != null) {
                jj_add_error_token(kind, i);
            }
        }
        if (ExpressionParser.jj_scanpos.kind != kind) {
            return true;
        }
        if (ExpressionParser.jj_la == 0 && ExpressionParser.jj_scanpos == ExpressionParser.jj_lastpos) {
            throw ExpressionParser.jj_ls;
        }
        return false;
    }
    
    public static final Token getNextToken() {
        if (ExpressionParser.token.next != null) {
            ExpressionParser.token = ExpressionParser.token.next;
        }
        else {
            final Token token = ExpressionParser.token;
            final ExpressionParserTokenManager token_source = ExpressionParser.token_source;
            final Token nextToken = ExpressionParserTokenManager.getNextToken();
            token.next = nextToken;
            ExpressionParser.token = nextToken;
        }
        ExpressionParser.jj_ntk = -1;
        ++ExpressionParser.jj_gen;
        return ExpressionParser.token;
    }
    
    public static final Token getToken(final int index) {
        Token t = ExpressionParser.lookingAhead ? ExpressionParser.jj_scanpos : ExpressionParser.token;
        for (int i = 0; i < index; ++i) {
            if (t.next != null) {
                t = t.next;
            }
            else {
                final Token token = t;
                final ExpressionParserTokenManager token_source = ExpressionParser.token_source;
                final Token nextToken = ExpressionParserTokenManager.getNextToken();
                token.next = nextToken;
                t = nextToken;
            }
        }
        return t;
    }
    
    private static final int jj_ntk() {
        if ((ExpressionParser.jj_nt = ExpressionParser.token.next) == null) {
            final Token token = ExpressionParser.token;
            final ExpressionParserTokenManager token_source = ExpressionParser.token_source;
            final Token nextToken = ExpressionParserTokenManager.getNextToken();
            token.next = nextToken;
            return ExpressionParser.jj_ntk = nextToken.kind;
        }
        return ExpressionParser.jj_ntk = ExpressionParser.jj_nt.kind;
    }
    
    private static void jj_add_error_token(final int kind, final int pos) {
        if (pos >= 100) {
            return;
        }
        if (pos == ExpressionParser.jj_endpos + 1) {
            ExpressionParser.jj_lasttokens[ExpressionParser.jj_endpos++] = kind;
        }
        else if (ExpressionParser.jj_endpos != 0) {
            ExpressionParser.jj_expentry = new int[ExpressionParser.jj_endpos];
            for (int i = 0; i < ExpressionParser.jj_endpos; ++i) {
                ExpressionParser.jj_expentry[i] = ExpressionParser.jj_lasttokens[i];
            }
            boolean exists = false;
            final Enumeration e = ExpressionParser.jj_expentries.elements();
            while (e.hasMoreElements()) {
                final int[] oldentry = e.nextElement();
                if (oldentry.length == ExpressionParser.jj_expentry.length) {
                    exists = true;
                    for (int j = 0; j < ExpressionParser.jj_expentry.length; ++j) {
                        if (oldentry[j] != ExpressionParser.jj_expentry[j]) {
                            exists = false;
                            break;
                        }
                    }
                    if (exists) {
                        break;
                    }
                    continue;
                }
            }
            if (!exists) {
                ExpressionParser.jj_expentries.addElement(ExpressionParser.jj_expentry);
            }
            if (pos != 0) {
                ExpressionParser.jj_lasttokens[(ExpressionParser.jj_endpos = pos) - 1] = kind;
            }
        }
    }
    
    public static ParseException generateParseException() {
        ExpressionParser.jj_expentries.removeAllElements();
        final boolean[] la1tokens = new boolean[19];
        for (int i = 0; i < 19; ++i) {
            la1tokens[i] = false;
        }
        if (ExpressionParser.jj_kind >= 0) {
            la1tokens[ExpressionParser.jj_kind] = true;
            ExpressionParser.jj_kind = -1;
        }
        for (int i = 0; i < 0; ++i) {
            if (ExpressionParser.jj_la1[i] == ExpressionParser.jj_gen) {
                for (int j = 0; j < 32; ++j) {
                    if ((ExpressionParser.jj_la1_0[i] & 1 << j) != 0x0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 19; ++i) {
            if (la1tokens[i]) {
                (ExpressionParser.jj_expentry = new int[1])[0] = i;
                ExpressionParser.jj_expentries.addElement(ExpressionParser.jj_expentry);
            }
        }
        ExpressionParser.jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        final int[][] exptokseq = new int[ExpressionParser.jj_expentries.size()][];
        for (int k = 0; k < ExpressionParser.jj_expentries.size(); ++k) {
            exptokseq[k] = ExpressionParser.jj_expentries.elementAt(k);
        }
        return new ParseException(ExpressionParser.token, exptokseq, ExpressionParser.tokenImage);
    }
    
    public static final void enable_tracing() {
    }
    
    public static final void disable_tracing() {
    }
    
    private static final void jj_rescan_token() {
        ExpressionParser.jj_rescan = true;
        for (int i = 0; i < 26; ++i) {
            JJCalls p = ExpressionParser.jj_2_rtns[i];
            do {
                if (p.gen > ExpressionParser.jj_gen) {
                    ExpressionParser.jj_la = p.arg;
                    ExpressionParser.jj_lastpos = (ExpressionParser.jj_scanpos = p.first);
                    switch (i) {
                        case 0: {
                            jj_3_1();
                            break;
                        }
                        case 1: {
                            jj_3_2();
                            break;
                        }
                        case 2: {
                            jj_3_3();
                            break;
                        }
                        case 3: {
                            jj_3_4();
                            break;
                        }
                        case 4: {
                            jj_3_5();
                            break;
                        }
                        case 5: {
                            jj_3_6();
                            break;
                        }
                        case 6: {
                            jj_3_7();
                            break;
                        }
                        case 7: {
                            jj_3_8();
                            break;
                        }
                        case 8: {
                            jj_3_9();
                            break;
                        }
                        case 9: {
                            jj_3_10();
                            break;
                        }
                        case 10: {
                            jj_3_11();
                            break;
                        }
                        case 11: {
                            jj_3_12();
                            break;
                        }
                        case 12: {
                            jj_3_13();
                            break;
                        }
                        case 13: {
                            jj_3_14();
                            break;
                        }
                        case 14: {
                            jj_3_15();
                            break;
                        }
                        case 15: {
                            jj_3_16();
                            break;
                        }
                        case 16: {
                            jj_3_17();
                            break;
                        }
                        case 17: {
                            jj_3_18();
                            break;
                        }
                        case 18: {
                            jj_3_19();
                            break;
                        }
                        case 19: {
                            jj_3_20();
                            break;
                        }
                        case 20: {
                            jj_3_21();
                            break;
                        }
                        case 21: {
                            jj_3_22();
                            break;
                        }
                        case 22: {
                            jj_3_23();
                            break;
                        }
                        case 23: {
                            jj_3_24();
                            break;
                        }
                        case 24: {
                            jj_3_25();
                            break;
                        }
                        case 25: {
                            jj_3_26();
                            break;
                        }
                    }
                }
                p = p.next;
            } while (p != null);
        }
        ExpressionParser.jj_rescan = false;
    }
    
    private static final void jj_save(final int index, final int xla) {
        JJCalls p;
        for (p = ExpressionParser.jj_2_rtns[index]; p.gen > ExpressionParser.jj_gen; p = p.next) {
            if (p.next == null) {
                final JJCalls jjCalls = p;
                final JJCalls next = new JJCalls();
                jjCalls.next = next;
                p = next;
                break;
            }
        }
        p.gen = ExpressionParser.jj_gen + xla - ExpressionParser.jj_la;
        p.first = ExpressionParser.token;
        p.arg = xla;
    }
    
    static {
        ExpressionParser.parser = null;
        ExpressionParser.jj_initialized_once = false;
        ExpressionParser.lookingAhead = false;
        jj_la1 = new int[0];
        jj_la1_0();
        jj_2_rtns = new JJCalls[26];
        ExpressionParser.jj_rescan = false;
        ExpressionParser.jj_gc = 0;
        jj_ls = new LookaheadSuccess();
        ExpressionParser.jj_expentries = new Vector();
        ExpressionParser.jj_kind = -1;
        ExpressionParser.jj_lasttokens = new int[100];
    }
    
    private static final class LookaheadSuccess extends Error
    {
    }
    
    static final class JJCalls
    {
        int gen;
        Token first;
        int arg;
        JJCalls next;
    }
}
