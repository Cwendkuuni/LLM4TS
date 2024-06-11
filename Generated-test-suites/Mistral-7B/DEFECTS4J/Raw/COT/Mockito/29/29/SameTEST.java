import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameMethodWithSameObjects() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = wantedObject;

        Assert.assertTrue("Should pass when matching the same object", sameMatcher.matches(actualObject));
    }

    @Test
    public void testSameMethodWithDifferentObjects() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = new Object();

        Assert.assertFalse("Should fail when matching different objects", sameMatcher.matches(actualObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameMethodWithNullWantedObject() {
        Same sameMatcher = new Same(null);

        throw new RuntimeException("Test should throw an IllegalArgumentException");
    }

    @Test
    public void testDescribeToMethodWithStringWantedObject() {
        Object wantedObject = "testString";
        Same sameMatcher = new Same(wantedObject);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(\"testString\")";
        String actualDescription = description.toString();

        Assert.assertEquals("Should generate correct description for String wanted object", expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToMethodWithCharacterWantedObject() {
        Character wantedCharacter = 'a';
        Same sameMatcher = new Same(wantedCharacter);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same('a')";
        String actualDescription = description.toString();

        Assert.assertEquals("Should generate correct description for Character wanted object", expectedDescription, actualDescription);
    }

    @Test
    public void testSameMethodWithSameArrayOfObjects() {
        Object wantedObject1 = new Object();
        Object wantedObject2 = new Object();
        Object[] wantedArray = new Object[]{wantedObject1, wantedObject2};
        Same sameMatcher = new Same(wantedArray);
        Object[] actualArray = new Object[]{wantedObject1, wantedObject2};

        Assert.assertTrue("Should pass when matching same array of objects", sameMatcher.matches(actualArray));
    }

    @Test
    public void testSameMethodWithDifferentArraysOfObjects() {
        Object wantedObject1 = new Object();
        Object wantedObject2 = new Object();
        Object[] wantedArray = new Object[]{wantedObject1, wantedObject2};
        Object[] actualArray1 = new Object[]{wantedObject1, new Object()};
        Object[] actualArray2 = new Object[]{new Object(), wantedObject2};
        Same sameMatcher = new Same(wantedArray);

        Assert.assertFalse("Should fail when matching different arrays of objects", sameMatcher.matches(actualArray1));
        Assert.assertFalse("Should fail when matching different arrays of objects", sameMatcher.matches(actualArray2));
    }

    @Test
    public void testSameMethodWithSameCollections() {
        Object element1 = new Object();
        Object element2 = new Object();
        java.util.List<Object> wantedList = new java.util.ArrayList<>();
        wantedList.add(element1);
        wantedList.add(element2);
        Same sameMatcher = new Same(wantedList);
        java.util.List<Object> actualList = new java.util.ArrayList<>();
        actualList.add(element1);
        actualList.add(element2);

        Assert.assertTrue("Should pass when matching same collections", sameMatcher.matches(actualList));
    }

    @Test
    public void testSameMethodWithDifferentCollections() {
        Object element1 = new Object();
        Object element2 = new Object();
        java.util.List<Object> wantedList = new java.util.ArrayList<>();
        wantedList.add(element1);
        wantedList.add(element2);
        Same sameMatcher = new Same(wantedList);
        java.util.List<Object> actualList1 = new java.util.ArrayList<>();
        actualList1.add(element1);
        actualList1.add(new Object());
        java.util.List<Object> actualList2 = new java.util.ArrayList<>();
        actualList2.add(new Object());
        actualList2.add(element2);

        Assert.assertFalse("Should fail when matching different collections", sameMatcher.matches(actualList1));
        Assert.assertFalse("Should fail when matching different collections", sameMatcher.matches(actualList2));
    }
}