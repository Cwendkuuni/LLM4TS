import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {
    @Test
    public void testDefaultConstructor() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        assertNotNull(xmlSerializerProvider);
    }
}