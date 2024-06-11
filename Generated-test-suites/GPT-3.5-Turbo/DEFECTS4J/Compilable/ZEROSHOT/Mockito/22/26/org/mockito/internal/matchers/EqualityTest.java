package org.mockito.internal.matchers;
import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects() {
        Assert.assertTrue(Equality.areEqual(null, null));
        Assert.assertFalse(Equality.areEqual(null, new Object()));
        Assert.assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_Arrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {2, 3, 4};

        Assert.assertTrue(Equality.areEqual(array1, array1));
        Assert.assertTrue(Equality.areEqual(array1, array2));
        Assert.assertFalse(Equality.areEqual(array1, array3));
        Assert.assertFalse(Equality.areEqual(array1, new Object()));
        Assert.assertFalse(Equality.areEqual(array1, null));
    }

    @Test
    public void testAreEqual_EqualObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        Assert.assertTrue(Equality.areEqual(obj1, obj2));
        Assert.assertTrue(Equality.areEqual(obj2, obj1));
    }

    @Test
    public void testAreEqual_UnequalObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Assert.assertFalse(Equality.areEqual(obj1, obj2));
        Assert.assertFalse(Equality.areEqual(obj2, obj1));
    }
}