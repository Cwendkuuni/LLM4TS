import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/loose.dtd";
        String baseUri = "https://www.example.com";

        // Act
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Assert
        // TODO: Add assertions to verify the correctness of the constructor
    }
}