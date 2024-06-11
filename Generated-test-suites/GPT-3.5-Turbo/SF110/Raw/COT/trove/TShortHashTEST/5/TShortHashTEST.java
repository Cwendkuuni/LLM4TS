import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        // Arrange
        TShortHash t = new TShortHash();
        t.add((short) 1);
        
        // Assert
        assertTrue(t.contains((short) 1));
        assertFalse(t.contains((short) 2));
    }
    
    @Test
    public void testForEach() {
        // Arrange
        TShortHash t = new TShortHash();
        t.add((short) 1);
        t.add((short) 2);
        
        // Act
        boolean result = t.forEach(new TShortProcedure() {
          public boolean execute(short arg) {
            return arg > 0;
          }
        });
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testRemoveAt() {
        // Arrange
        TShortHash t = new TShortHash();
        t.add((short) 1);
        t.add((short) 2);
        
        // Act
        t.removeAt(0);
        
        // Assert
        assertEquals(2, t.get(0));
    }
    
    @Test
    public void testIndex() {
        // Arrange
        TShortHash t = new TShortHash();
        t.add((short) 1);
        
        // Act
        int result = t.index((short) 1);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testIndexRehashed() {
        // Arrange
        TShortHash t = new TShortHash();
        t.add((short) 1);
        t.add((short) 2);
        
        // Act
        int result = t.indexRehashed((short) 2, 0, 1, (byte) 1);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testInsertKey() {
        // Arrange
        TShortHash t = new TShortHash();
        
        // Act
        int result = t.insertKey((short) 1);
        
        // Assert
        assertEquals(0, result);
        assertEquals(1, t.get(0));
    }
    
    @Test
    public void testInsertKeyRehash() {
        // Arrange
        TShortHash t = new TShortHash();
        t.add((short) 1);
        t.add((short) 2);
        t.add((short) 3);
        
        // Act
        int result = t.insertKeyRehash((short) 4, 0, 2, (byte) 1);
        
        // Assert
        assertEquals(1, result);
        assertEquals(4, t.get(1));
    }
    
    @Test
    public void testInsertKeyAt() {
        // Arrange
        TShortHash t = new TShortHash();
        
        // Act
        t.insertKeyAt(0, (short) 1);
        
        // Assert
        assertEquals(1, t.get(0));
    }
}
