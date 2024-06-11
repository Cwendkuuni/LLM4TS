package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;

public abstract class Expression
{
    protected String expression;
    protected ExpressionStack stack;
    protected List externalFunctions;
    protected static Map reservedFunctions;
    boolean prepared;
    
    public List getExternalFunctions() {
        return this.externalFunctions;
    }
    
    public Expression(final String s) {
        this.prepared = false;
        this.stack = new ExpressionStack();
        this.expression = s;
    }
    
    protected void doEvaluationStack() {
        final Stack<String> operands = new Stack<String>();
        final ExpressionStack values = new ExpressionStack();
        int input = 0;
        final int size = this.stack.size();
        int parentesis = 0;
        while (input < size) {
            final String expression = this.stack.get(input);
            if (JavexUtils.isReservedChar(expression) || this.isFunction(expression)) {
                if (expression.equals("(")) {
                    ++parentesis;
                    operands.push(expression);
                }
                else if (expression.equals(")")) {
                    boolean b = true;
                    while (!operands.isEmpty()) {
                        if (!b) {
                            break;
                        }
                        final String op = operands.peek();
                        if (!op.equals("(")) {
                            values.push(operands.pop());
                        }
                        else {
                            operands.pop();
                            b = false;
                            --parentesis;
                        }
                    }
                }
                else if (expression.equals("+") || expression.equals("-")) {
                    boolean b = true;
                    while (!operands.isEmpty() && b) {
                        final String op = operands.peek();
                        if (op.equals("+") || op.equals("-") || op.equals("(")) {
                            b = false;
                        }
                        if (!op.equals("(")) {
                            values.push(operands.pop());
                        }
                    }
                    operands.push(expression);
                }
                else if ((expression.equalsIgnoreCase("and") || expression.equalsIgnoreCase("or")) && parentesis == 0) {
                    boolean b = true;
                    while (!operands.isEmpty() && b) {
                        final String op = operands.peek();
                        if (op.equalsIgnoreCase("and") || op.equalsIgnoreCase("or")) {
                            b = false;
                        }
                        values.push(operands.pop());
                    }
                    operands.push(expression);
                }
                else {
                    boolean b = true;
                    if (this.isLogicalFunction(expression)) {
                        while (!operands.isEmpty() && b) {
                            final String op = operands.peek();
                            if (!this.isLogicalFunction(op) && !op.equals("(")) {
                                values.push(operands.pop());
                            }
                            else {
                                b = false;
                            }
                        }
                    }
                    operands.push(expression);
                }
            }
            else {
                values.push(expression);
            }
            ++input;
        }
        while (!operands.isEmpty()) {
            values.push(operands.pop());
        }
        this.stack = values;
    }
    
    private static void putFunction(final FunctionExpression f, final Map map) {
        map.put(f, f);
    }
    
    public static FunctionExpression getReservedFunctions(final String simbol) {
        if (Expression.reservedFunctions == null) {
            Expression.reservedFunctions = new TreeMap();
            putFunction(new AndFunction(), Expression.reservedFunctions);
            putFunction(new BetweenFunction(), Expression.reservedFunctions);
            putFunction(new ContainsFunction(), Expression.reservedFunctions);
            putFunction(new EndsWithFunction(), Expression.reservedFunctions);
            putFunction(new EqualsFunction(), Expression.reservedFunctions);
            putFunction(new GreaterEqThanFunction(), Expression.reservedFunctions);
            putFunction(new GreaterThanFunction(), Expression.reservedFunctions);
            putFunction(new IsEmptyFunction(), Expression.reservedFunctions);
            putFunction(new IsFunction(), Expression.reservedFunctions);
            putFunction(new IsNullFunction(), Expression.reservedFunctions);
            putFunction(new LessEqThanFunction(), Expression.reservedFunctions);
            putFunction(new LessThanFunction(), Expression.reservedFunctions);
            putFunction(new NotEqualsFunction(), Expression.reservedFunctions);
            putFunction(new NotFunction(), Expression.reservedFunctions);
            putFunction(new NotCharFunction(), Expression.reservedFunctions);
            putFunction(new OrFunction(), Expression.reservedFunctions);
            putFunction(new StartsWithFunction(), Expression.reservedFunctions);
            putFunction(new MatchesFunction(), Expression.reservedFunctions);
            putFunction(new InListFunction(), Expression.reservedFunctions);
            putFunction(new AbsFunction(), Expression.reservedFunctions);
            putFunction(new DivideFunction(), Expression.reservedFunctions);
            putFunction(new MultiplyFunction(), Expression.reservedFunctions);
            putFunction(new PlusFunction(), Expression.reservedFunctions);
            putFunction(new SqrtFunction(), Expression.reservedFunctions);
            putFunction(new SubstractFunction(), Expression.reservedFunctions);
            putFunction(new PowerFunction(), Expression.reservedFunctions);
            putFunction(new PowerCharFunction(), Expression.reservedFunctions);
            putFunction(new DateAddFunction(), Expression.reservedFunctions);
            putFunction(new DateDiffFunction(), Expression.reservedFunctions);
            putFunction(new LengthFunction(), Expression.reservedFunctions);
            putFunction(new SubStringFunction(), Expression.reservedFunctions);
            putFunction(new ToLowerFunction(), Expression.reservedFunctions);
            putFunction(new ToUpperFunction(), Expression.reservedFunctions);
        }
        final FunctionExpression comp = new GenericFunctionExpression(simbol);
        return Expression.reservedFunctions.get(comp);
    }
    
    public boolean isReservedFunction(final String simbol) {
        final Object o = getReservedFunctions(simbol);
        return o != null;
    }
    
    public boolean isLogicalFunction(final String simbol) {
        final FunctionExpression o = this.getFunction(simbol);
        return o != null && o.getType() == FunctionExpression.LOGICAL;
    }
    
    public boolean isMathFunction(final String simbol) {
        final FunctionExpression o = this.getFunction(simbol);
        return o != null && o.getType() == FunctionExpression.MATH;
    }
    
    public boolean isDateFunction(final String simbol) {
        final FunctionExpression o = this.getFunction(simbol);
        return o != null && o.getType() == FunctionExpression.DATE;
    }
    
    public boolean isStringFunction(final String simbol) {
        final FunctionExpression o = this.getFunction(simbol);
        return o != null && o.getType() == FunctionExpression.STRING;
    }
    
    public FunctionExpression getExternalFunction(final String simbol) {
        FunctionExpression f = null;
        if (this.externalFunctions != null) {
            final Iterator it = this.externalFunctions.iterator();
            while (it.hasNext()) {
                f = it.next();
                if (f.getSimbol().equalsIgnoreCase(simbol)) {
                    return f;
                }
            }
        }
        return null;
    }
    
    public boolean isExternalFunction(final String simbol) {
        final FunctionExpression f = this.getExternalFunction(simbol);
        return f != null;
    }
    
    public FunctionExpression getFunction(final String simbol) {
        FunctionExpression f = null;
        f = getReservedFunctions(simbol);
        if (f != null) {
            return f;
        }
        f = this.getExternalFunction(simbol);
        return f;
    }
    
    public boolean isFunction(final String simbol) {
        final FunctionExpression f = this.getFunction(simbol);
        return f != null;
    }
    
    public Expression() {
        this.prepared = false;
    }
    
    private void stack(final String c, final Stack stack) {
        if (!stack.isEmpty()) {
            final String aux = stack.peek();
            stack.pop();
            stack.push(String.valueOf(aux) + c);
        }
        else {
            stack.push(c);
        }
    }
    
    public void prepare() {
        this.tokenize();
        this.doEvaluationStack();
        this.prepared = true;
    }
    
    private ExpressionStack getStackCopy() {
        final ExpressionStack st = new ExpressionStack();
        for (int n = 0; n < this.stack.size(); ++n) {
            st.add(new String(this.stack.get(n).toString()));
        }
        return st;
    }
    
    public abstract Object evaluate() throws Exception;
    
    private void tokenize() {
        boolean quote = false;
        if (this.stack == null) {
            this.stack = new ExpressionStack();
        }
        else {
            this.stack.clear();
        }
        for (int n = 0; n < this.expression.length(); ++n) {
            final String c = new StringBuilder().append(this.expression.charAt(n)).toString();
            final boolean reserved = JavexUtils.isReservedChar(c);
            final boolean isSpace = JavexUtils.isSpace(c);
            final boolean isQuote = JavexUtils.isQuote(c);
            if (isQuote && quote) {
                quote = false;
                this.stack(c, this.stack);
            }
            else if (isQuote && !quote) {
                quote = true;
                this.stack.push(c);
            }
            else if (reserved && quote) {
                this.stack(c, this.stack);
            }
            else if (reserved && !quote) {
                if (!this.stack.isEmpty()) {
                    if (this.isFunction(String.valueOf(this.stack.peek()) + c)) {
                        this.stack.push(String.valueOf(this.stack.pop()) + c);
                    }
                    else {
                        this.stack.push(c);
                    }
                }
                else {
                    this.stack.push(c);
                }
            }
            else if (!isQuote && quote) {
                this.stack(c, this.stack);
            }
            else if (!isSpace && !this.stack.isEmpty() && !this.isFunction(this.stack.peek()) && !JavexUtils.isReservedChar(this.stack.peek())) {
                this.stack.push(String.valueOf(this.stack.pop()) + c);
            }
            else if (!isSpace && (this.stack.isEmpty() || (!this.stack.isEmpty() && (JavexUtils.isReservedChar(this.stack.peek()) || this.isFunction(this.stack.peek()))))) {
                this.stack.push(c);
            }
            else if (isSpace) {
                this.stack.push(c);
            }
        }
        final ExpressionStack st = new ExpressionStack();
        for (int n2 = 0; n2 < this.stack.size(); ++n2) {
            final String s1 = this.stack.get(n2).trim();
            if (s1.length() > 0) {
                st.add(s1);
            }
        }
        this.stack = st;
        for (int n2 = 0; n2 < this.stack.size(); ++n2) {
            final String s1 = this.stack.get(n2);
            if (JavexUtils.isNumber(s1) && n2 >= 1) {
                final String s2 = this.stack.get(n2 - 1);
                final String ss1 = (n2 > 1) ? this.stack.get(n2 - 2) : "(";
                final boolean b = JavexUtils.isReservedChar(ss1);
                if ((s2.equals("-") || s2.equals("+")) && b) {
                    this.stack.set(n2 - 1, String.valueOf(s2) + s1);
                    this.stack.remove(n2);
                    --n2;
                }
            }
        }
        for (int m = this.stack.size(), n3 = 0; n3 < m; ++n3) {
            if (this.stack.get(n3).equals(",") && n3 + 1 < this.stack.size() && n3 > 0) {
                final String a = String.valueOf(this.stack.get(n3 - 1)) + this.stack.get(n3) + this.stack.get(n3 + 1);
                this.stack.set(n3 - 1, a);
                this.stack.remove(n3);
                this.stack.remove(n3);
                m -= 2;
                --n3;
            }
        }
    }
}
