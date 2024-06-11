package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 1));
        expectedResult.addLink(new StringLink(4, 2, 4));
        RelativeString actualResult = StringIncrementor.diff("hello", "heylo");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new RelativeString();
        actualResult = StringIncrementor.diff("hello", "hello");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 3));
        expectedResult.addLink(new StringLink(5, 4, 2));
        actualResult = StringIncrementor.diff("hello", "helpo");
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testDiffWithVerbose() {
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 1));
        expectedResult.addLink(new StringLink(4, 2, 4));
        RelativeString actualResult = StringIncrementor.diff("hello", "heylo", true);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new RelativeString();
        actualResult = StringIncrementor.diff("hello", "hello", true);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 3));
        expectedResult.addLink(new StringLink(5, 4, 2));
        actualResult = StringIncrementor.diff("hello", "helpo", true);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testPatch() {
        String expectedResult = "heylo";
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 1));
        relativeString.addLink(new StringLink(4, 2, 4));
        String actualResult = StringIncrementor.patch("hello", relativeString);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = "hello";
        relativeString = new RelativeString();
        actualResult = StringIncrementor.patch("hello", relativeString);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = "helpo";
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.addLink(new StringLink(5, 4, 2));
        actualResult = StringIncrementor.patch("hello", relativeString);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testPatchWithVerbose() {
        String expectedResult = "heylo";
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 1));
        relativeString.addLink(new StringLink(4, 2, 4));
        String actualResult = StringIncrementor.patch("hello", relativeString, true);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = "hello";
        relativeString = new RelativeString();
        actualResult = StringIncrementor.patch("hello", relativeString, true);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = "helpo";
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.addLink(new StringLink(5, 4, 2));
        actualResult = StringIncrementor.patch("hello", relativeString, true);
        assertEquals(expectedResult, actualResult);
    }
}