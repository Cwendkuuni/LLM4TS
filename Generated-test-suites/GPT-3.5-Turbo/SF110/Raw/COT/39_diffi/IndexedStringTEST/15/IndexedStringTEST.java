
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    @Test
    public void testIndexOf() {
        // Test case 1: valid target in the middle of the string
        IndexedString indexedString1 = new IndexedString("abcdefg");
        char[] target1 = {'c', 'd', 'e'};
        int expectedResult1 = 2;
        assertEquals(expectedResult1, indexedString1.indexOf(target1));
        
        // Test case 2: valid target at the beginning of the string
        IndexedString indexedString2 = new IndexedString("abcdefg");
        char[] target2 = {'a', 'b', 'c'};
        int expectedResult2 = 0;
        assertEquals(expectedResult2, indexedString2.indexOf(target2));
        
        // Test case 3: valid target at the end of the string
        IndexedString indexedString3 = new IndexedString("abcdefg");
        char[] target3 = {'e', 'f', 'g'};
        int expectedResult3 = 4;
        assertEquals(expectedResult3, indexedString3.indexOf(target3));
        
        // Test case 4: invalid target that does not exist in the string
        IndexedString indexedString4 = new IndexedString("abcdefg");
        char[] target4 = {'x', 'y', 'z'};
        int expectedResult4 = -1;
        assertEquals(expectedResult4, indexedString4.indexOf(target4));
        
        // Test case 5: empty target
        IndexedString indexedString5 = new IndexedString("abcdefg");
        char[] target5 = {};
        int expectedResult5 = -1;
        assertEquals(expectedResult5, indexedString5.indexOf(target5));
    }
}
