import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {
    @Test
    public void testXmlSerializerProviderCreationWithXmlRootNameLookup() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNames);
        assertNotNull(xmlSerializerProvider);
    }
}