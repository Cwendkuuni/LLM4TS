
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = createDirtyDocument();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // assertion to check if cleanDocument only contains elements allowed by the whitelist
        validateCleanDocument(cleanDocument, whitelist);
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = createDirtyDocument();
        
        // assertion to check if the dirtyDocument is valid against the whitelist
        assertTrue(cleaner.isValid(dirtyDocument));
    }
    
    private Whitelist createWhitelist() {
        // create and return a whitelist with allowed tags and attributes
    }
    
    private Document createDirtyDocument() {
        // create and return a dirty document
    }
    
    private void validateCleanDocument(Document cleanDocument, Whitelist whitelist) {
        // validate if cleanDocument only contains elements allowed by the whitelist
    }
}
