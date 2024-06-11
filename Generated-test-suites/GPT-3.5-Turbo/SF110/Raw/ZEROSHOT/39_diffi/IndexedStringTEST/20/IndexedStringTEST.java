package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("test");
    }

    @Test
    public void testIndexOf() {
        char[] target = {'t', 'e', 's', 't'};
        int expectedIndex = 0;
        int actualIndex = indexedString.indexOf(target);
        Assert.assertEquals(expectedIndex, actualIndex);

        char[] invalidTarget = {'a', 'b', 'c', 'd'};
        int expectedInvalidIndex = -1;
        int actualInvalidIndex = indexedString.indexOf(invalidTarget);
        Assert.assertEquals(expectedInvalidIndex, actualInvalidIndex);
    }
}