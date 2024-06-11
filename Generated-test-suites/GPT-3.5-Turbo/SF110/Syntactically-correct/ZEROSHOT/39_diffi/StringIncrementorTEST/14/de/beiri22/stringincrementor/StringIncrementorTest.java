import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;
import org.junit.Assert;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "fghij";
        RelativeString result = StringIncrementor.diff(a, b);
        Assert.assertEquals(result.getLinks().size(), 0);
        
        a = "abcde";
        b = "abcde";
        result = StringIncrementor.diff(a, b);
        Assert.assertEquals(result.getLinks().size(), 0);
        
        a = "abcde";
        b = "abcdex";
        result = StringIncrementor.diff(a, b);
        Assert.assertEquals(result.getLinks().size(), 1);
        Assert.assertEquals(result.getLinks().get(0).getPosOrig(), 4);
        Assert.assertEquals(result.getLinks().get(0).getPosNew(), 5);
        Assert.assertEquals(result.getLinks().get(0).getLen(), 1);
        
        a = "abcdefghijklmnopqrstuvwxyz";
        b = "abcdefghijklemnopqrstuvwxyz";
        result = StringIncrementor.diff(a, b);
        Assert.assertEquals(result.getLinks().size(), 1);
        Assert.assertEquals(result.getLinks().get(0).getPosOrig(), 11);
        Assert.assertEquals(result.getLinks().get(0).getPosNew(), 11);
        Assert.assertEquals(result.getLinks().get(0).getLen(), 12);
        
        // Add more test cases for different scenarios
    }
    
    @Test
    public void testPatch() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        Assert.assertEquals(result, "");
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 5));
        result = StringIncrementor.patch(a, r);
        Assert.assertEquals(result, a);
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(1, 1, 2));
        result = StringIncrementor.patch(a, r);
        Assert.assertEquals(result, "abbcde");
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        r.addLink(new StringLink(3, 3, 2));
        result = StringIncrementor.patch(a, r);
        Assert.assertEquals(result, "abcde");
        
        // Add more test cases for different scenarios
    }
}