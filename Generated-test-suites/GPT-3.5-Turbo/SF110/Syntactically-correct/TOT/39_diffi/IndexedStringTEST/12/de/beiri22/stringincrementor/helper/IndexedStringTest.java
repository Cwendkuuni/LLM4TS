
package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    // Test case for count method
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        int result = indexedString.count('l');
        Assert.assertEquals(2, result);
    }

    // Test case for count method with no occurrence of the given character
    @Test
    public void testCountNoOccurrence() {
        IndexedString indexedString = new IndexedString("hello");
        int result = indexedString.count('z');
        Assert.assertEquals(0, result);
    }

    // Test case for count method with empty string
    @Test
    public void testCountEmptyString() {
        IndexedString indexedString = new IndexedString("");
        int result = indexedString.count('z');
        Assert.assertEquals(0, result);
    }

    // Test case for IndexedString constructor
    @Test
    public void testIndexedStringConstructor() {
        IndexedString indexedString = new IndexedString("hello");
        char[] expectedValues = {'h', 'e', 'l', 'l', 'o'};
        int[][] expectedIndex = new int[256][];
        expectedIndex['h'] = new int[]{0};
        expectedIndex['e'] = new int[]{1};
        expectedIndex['l'] = new int[]{2, 3};
        expectedIndex['o'] = new int[]{4};

        Assert.assertArrayEquals(expectedValues, indexedString.getValues());
        Assert.assertArrayEquals(expectedIndex, indexedString.getIndex());
    }

    // Test case for IndexedString constructor with empty string
    @Test
    public void testIndexedStringConstructorEmptyString() {
        IndexedString indexedString = new IndexedString("");
        char[] expectedValues = {};
        int[][] expectedIndex = new int[256][];
        
        Assert.assertArrayEquals(expectedValues, indexedString.getValues());
        Assert.assertArrayEquals(expectedIndex, indexedString.getIndex());
    }

    // Test case for indexOf method with a target present
    @Test
    public void testIndexOfWithTargetPresent() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'o'};
        int result = indexedString.indexOf(target);
        Assert.assertEquals(2, result);
    }

    // Test case for indexOf method with target not present
    @Test
    public void testIndexOfWithTargetNotPresent() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'z'};
        int result = indexedString.indexOf(target);
        Assert.assertEquals(-1, result);
    }

    // Test case for indexOf method with empty target
    @Test
    public void testIndexOfWithEmptyTarget() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {};
        int result = indexedString.indexOf(target);
        Assert.assertEquals(-1, result);
    }
}

