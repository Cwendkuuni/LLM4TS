import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

public class CleanerTest {

    @Test
    public void clean_ValidInput_ShouldReturnCleanedDocument() {
        // Arrange
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><head></head><body><div>Test</div></body></html>");
        
        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert
        // Check that the clean document only contains the cleaned body elements
        assert cleanDocument.select("body").size() == 1;
        assert cleanDocument.select("div").size() == 1;
        assert cleanDocument.select("div").first().text().equals("Test");
    }

    @Test
    public void clean_NullInput_ShouldThrowException() {
        // Arrange
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            cleaner.clean(null);
        });
    }

    @Test
    public void isValid_ValidInput_ShouldReturnTrue() {
        // Arrange
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><head></head><body><div>Test</div></body></html>");
        
        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert
        assert isValid == true;
    }

    @Test
    public void isValid_InputWithUnsafeTag_ShouldReturnFalse() {
        // Arrange
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><head></head><body><script>Malicious code</script></body></html>");
        
        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert
        assert isValid == false;
    }

    @Test
    public void isValid_NullInput_ShouldThrowException() {
        // Arrange
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            cleaner.isValid(null);
        });
    }
}