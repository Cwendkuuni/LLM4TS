package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermConstructor1() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("?"));
        JSTerm jsTerm = new JSTerm(tokenizer);
        
        Assert.assertFalse(jsTerm.isVariable());
        Assert.assertFalse(jsTerm.isConstant());
        Assert.assertTrue(jsTerm.isFunction());
        Assert.assertFalse(jsTerm.isEval());
    }
    
    @Test
    public void testJSTermConstructor2() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("%"));
        JSTerm jsTerm = new JSTerm(tokenizer);
        
        Assert.assertFalse(jsTerm.isVariable());
        Assert.assertTrue(jsTerm.isConstant());
        Assert.assertFalse(jsTerm.isFunction());
        Assert.assertFalse(jsTerm.isEval());
    }
    
    @Test
    public void testJSTermConstructor3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("1"));
        JSTerm jsTerm = new JSTerm(tokenizer);
        
        Assert.assertFalse(jsTerm.isVariable());
        Assert.assertTrue(jsTerm.isConstant());
        Assert.assertFalse(jsTerm.isFunction());
        Assert.assertFalse(jsTerm.isEval());
    }
    
    // Continue with tests for other methods

}