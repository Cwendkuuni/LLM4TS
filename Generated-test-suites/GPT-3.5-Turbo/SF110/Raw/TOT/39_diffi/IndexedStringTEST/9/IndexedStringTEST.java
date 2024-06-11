
package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        this.indexedString = new IndexedString("abaababaaba");
    }

    @Test
    public void testCount_validInput_returnsCorrectCount() {
        int count = indexedString.count('a');
        Assert.assertEquals(7, count);

        count = indexedString.count('b');
        Assert.assertEquals(4, count);

        count = indexedString.count('c');
        Assert.assertEquals(0, count);
    }

    @Test
    public void testIndexFirstOccurrence_validInput_returnsCorrectIndex() {
        int index = indexedString.indexOf(new char[]{'a', 'b', 'a'});
        Assert.assertEquals(0, index);

        index = indexedString.indexOf(new char[]{'b', 'a'});
        Assert.assertEquals(1, index);

        index = indexedString.indexOf(new char[]{'a', 'b', 'a', 'a'});
        Assert.assertEquals(0, index);
    }

    @Test
    public void testIndexFirstOccurrence_targetNotFound_returnsMinusOne() {
        int index = indexedString.indexOf(new char[]{'c', 'd'});
        Assert.assertEquals(-1, index);

        index = indexedString.indexOf(new char[]{'a', 'b', 'c'});
        Assert.assertEquals(-1, index);

        index = indexedString.indexOf(new char[]{'a', 'b', 'a', 'c'});
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexFirstOccurrence_targetAtEnd_returnsCorrectIndex() {
        int index = indexedString.indexOf(new char[]{'a', 'a'});
        Assert.assertEquals(9, index);

        index = indexedString.indexOf(new char[]{'b', 'a', 'a'});
        Assert.assertEquals(6, index);

        index = indexedString.indexOf(new char[]{'a', 'a', 'b', 'a'});
        Assert.assertEquals(7, index);
    }

    @Test
    public void testIndexFirstOccurrence_emptyTarget_returnsMinusOne() {
        int index = indexedString.indexOf(new char[]{});
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexFirstOccurrence_nullTarget_returnsMinusOne() {
        int index = indexedString.indexOf(null);
        Assert.assertEquals(-1, index);
    }
}
