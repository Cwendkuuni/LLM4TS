package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.stream.StreamTokenizer;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormDefaultConstructor() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        Assert.assertEquals(0, predicateForm.size());
    }

    @Test
    public void testJSPredicateFormTextConstructor() {
        String text = "()";
        JSPredicateForm predicateForm = new JSPredicateForm(text);
        Assert.assertEquals(0, predicateForm.size());
    }

    @Test
    public void testJSPredicateFormTokenizerConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
        Assert.assertEquals(0, predicateForm.size());
    }

    // TODO: Add more test cases to cover all methods in JSPredicateForm class

}