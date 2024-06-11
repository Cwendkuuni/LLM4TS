
package de.beiri22.stringincrementor.helper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("hello world");
    }

    @Test
    public void testCount_typicalCase() {
        char c = 'l';
        int expectedResult = 3;

        int result = indexedString.count(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testCount_edgeCase() {
        char c = 'o';
        int expectedResult = 2;

        int result = indexedString.count(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testCount_nonExistentChar() {
        char c = 'z';
        int expectedResult = 0;

        int result = indexedString.count(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIndexedString_typicalCase() {
        String s = "hello world";
        int[][] expectedIndex = new int[256][];
        expectedIndex[104] = new int[]{ 2, 3 };
        expectedIndex[101] = new int[]{ 1 };
        expectedIndex[108] = new int[]{ 0 };
        expectedIndex[111] = new int[]{ 4, 7 };

        IndexedString indexedString = new IndexedString(s);

        assertEquals(s.toCharArray(), indexedString.values);
        assertEquals(expectedIndex, indexedString.index);
    }

    @Test
    public void testIndexedString_emptyString() {
        String s = "";
        int[][] expectedIndex = new int[256][];

        IndexedString indexedString = new IndexedString(s);

        assertEquals(s.toCharArray(), indexedString.values);
        assertEquals(expectedIndex, indexedString.index);
    }

    @Test
    public void testIndexOf_typicalCaseStartIndex() {
        char[] target = {'o', 'r', 'l', 'd'};
        int expectedResult = 7;

        int result = indexedString.indexOf(target);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIndexOf_typicalCaseMiddleIndex() {
        char[] target = {'l', 'o', ' '};
        int expectedResult = 2;

        int result = indexedString.indexOf(target);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIndexOf_typicalCaseEndIndex() {
        char[] target = {'d'};
        int expectedResult = 10;

        int result = indexedString.indexOf(target);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIndexOf_edgeCaseEmptyTarget() {
        char[] target = {};
        int expectedResult = -1;

        int result = indexedString.indexOf(target);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIndexOf_nonExistentChar() {
        char[] target = {'z'};
        int expectedResult = -1;

        int result = indexedString.indexOf(target);

        assertEquals(expectedResult, result);
    }
}
