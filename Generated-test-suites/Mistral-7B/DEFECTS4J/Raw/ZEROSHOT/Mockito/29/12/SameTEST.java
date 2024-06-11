import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object testObject = new Object();
        Same sameMatcher = new Same(testObject);

        Assert.assertTrue(sameMatcher.matches(testObject));
    }

    @Test
    public void testDifferentInstances() {
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        Same sameMatcher = new Same(testObject1);

        Assert.assertFalse(sameMatcher.matches(testObject2));
    }

    @Test
    public void testDifferentTypes() {
        Object testObject1 = new Object();
        Integer testObject2 = 1;
        Same sameMatcher = new Same(testObject1);

        Assert.assertFalse(sameMatcher.matches(testObject2));
    }

    @Test
    public void testSameStringValue() {
        String testObject1 = "test string";
        String testObject2 = "test string";
        Same sameMatcher = new Same(testObject1);

        Assert.assertTrue(sameMatcher.matches(testObject2));
    }

    @Test
    public void testSameCharacterValue() {
        Character testObject1 = 'a';
        Character testObject2 = 'a';
        Same sameMatcher = new Same(testObject1);

        Assert.assertTrue(sameMatcher.matches(testObject2));
    }

    @Test
    public void testDescribeTo() {
        Object testObject = new Object();
        Same sameMatcher = new Same(testObject);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(testObject)";
        String actualDescription = description.toString();

        Assert.assertEquals(expectedDescription, actualDescription);
    }
}