
package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount_WithValidInput_ReturnsCorrectCount() {
        IndexedString indexedString = new IndexedString("aabbc");
        
        int countA = indexedString.count('a');
        int countB = indexedString.count('b');
        int countC = indexedString.count('c');
        
        Assert.assertEquals(2, countA);
        Assert.assertEquals(2, countB);
        Assert.assertEquals(1, countC);
    }
    
    @Test
    public void testIndexedString_WithValidInput_CorrectlyInitializesValuesAndIndex() {
        String input = "aabbc";
        
        IndexedString indexedString = new IndexedString(input);
        
        Assert.assertArrayEquals(input.toCharArray(), indexedString.values);
        Assert.assertEquals(2, indexedString.index['a'].length);
        Assert.assertEquals(2, indexedString.index['b'].length);
        Assert.assertEquals(1, indexedString.index['c'].length);
    }
    
    @Test
    public void testIndexedString_WithEmptyString_CorrectlyInitializesValuesAndIndex() {
        String input = "";
        
        IndexedString indexedString = new IndexedString(input);
        
        Assert.assertArrayEquals(input.toCharArray(), indexedString.values);
        Assert.assertNotNull(indexedString.index['\0']);
        Assert.assertEquals(0, indexedString.index['\0'].length);
    }
    
    @Test
    public void testIndexOf_WithExistingTarget_ReturnsCorrectIndex() {
        IndexedString indexedString = new IndexedString("aabbc");
        char[] target = {'b', 'b'};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(3, index);
    }
    
    @Test
    public void testIndexOf_WithNonExistingTarget_ReturnsNegativeIndex() {
        IndexedString indexedString = new IndexedString("aabbc");
        char[] target = {'d', 'd'};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_WithEmptyString_ReturnsNegativeIndex() {
        IndexedString indexedString = new IndexedString("");
        char[] target = {'a', 'b', 'c'};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_WithEmptyTarget_ReturnsFirstIndex() {
        IndexedString indexedString = new IndexedString("aabbc");
        char[] target = {};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(0, index);
    }
    
    @Test
    public void testIndexOf_WithTargetExceedingMax_ReturnsNegativeIndex() {
        IndexedString indexedString = new IndexedString("aabbc");
        char[] target = {'a', 'b', 'b', 'c'};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_WithLastOccurrenceOfTarget_ReturnsCorrectIndex() {
        IndexedString indexedString = new IndexedString("aabbcde");
        char[] target = {'e'};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(6, index);
    }
    
    @Test
    public void testIndexOf_WithFirstOccurrenceOfTarget_ReturnsCorrectIndex() {
        IndexedString indexedString = new IndexedString("aabbcde");
        char[] target = {'a'};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(0, index);
    }
    
    @Test
    public void testIndexOf_WithMultipleOccurrencesOfTarget_ReturnsFirstOccurrenceIndex() {
        IndexedString indexedString = new IndexedString("aabbcde");
        char[] target = {'b'};
        
        int index = indexedString.indexOf(target);
        
        Assert.assertEquals(1, index);
    }
}

