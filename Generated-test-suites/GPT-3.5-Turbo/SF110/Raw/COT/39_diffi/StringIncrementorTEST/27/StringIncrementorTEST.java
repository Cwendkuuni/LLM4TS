
package de.beiri22.stringincrementor;

import org.junit.Assert;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {

        String a = "abcdefg";
        String b = "abcxyzdefg";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(3, 3, 3));
        expected.setAbsolute("xyz");
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {

        String a = "abcdefg";
        String b = "abcxyzdefg";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(3, 3, 3));
        expected.setAbsolute("xyz");
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatch() {

        String a = "abcdefg";
        String b = "abcxyzdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 3));
        r.setAbsolute("xyz");
        
        String expected = "abcxyzdefg";
        
        String result = StringIncrementor.patch(a, r);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatchVerbose() {

        String a = "abcdefg";
        String b = "abcxyzdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 3));
        r.setAbsolute("xyz");
        
        String expected = "abcxyzdefg";
        
        String result = StringIncrementor.patch(a, r, true);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiff_EmptyStrings() {

        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("");
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatch_EmptyStrings() {

        String a = "";
        String b = "";
        RelativeString r = new RelativeString();
        r.setAbsolute("");
        
        String expected = "";
        
        String result = StringIncrementor.patch(a, r);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDiff_StringBLongerThanA() {

        String a = "abc";
        String b = "abcdefg";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 3));
        expected.setAbsolute("defg");
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testPatch_StringBLongerThanA() {

        String a = "abc";
        String b = "abcdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        r.setAbsolute("defg");
        
        String expected = "abcdefg";
        
        String result = StringIncrementor.patch(a, r);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiff_StringBShorterThanA() {

        String a = "abcdefg";
        String b = "abc";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("");
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testPatch_StringBShorterThanA() {

        String a = "abcdefg";
        String b = "abc";
        RelativeString r = new RelativeString();
        r.setAbsolute("");
        
        String expected = "abcdefg";
        
        String result = StringIncrementor.patch(a, r);
        
        Assert.assertEquals(expected, result);
    }
    
}
