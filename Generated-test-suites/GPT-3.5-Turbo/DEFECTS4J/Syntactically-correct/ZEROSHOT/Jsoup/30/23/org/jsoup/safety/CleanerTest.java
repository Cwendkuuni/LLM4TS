import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }
    
    @Test
    public void testClean() {
        // Arrange
        Document dirtyDocument = new Document("");
        
        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert
        assertNotNull(cleanDocument);
    }
    
    @Test
    public void testIsValid() {
        // Arrange
        Document dirtyDocument = new Document("");
        
        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        // Arrange
        Element root = new Element(Tag.valueOf("body"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        
        // Act
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        // Assert
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        // Arrange
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        
        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        // Assert
        assertNotNull(elementMeta);
    }

}