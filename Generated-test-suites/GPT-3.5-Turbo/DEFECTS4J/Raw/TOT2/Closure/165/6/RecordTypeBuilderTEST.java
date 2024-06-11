import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("name", JSType.STRING, new Node());
        
        assertNotNull(builder.build());
    }

    @Test
    public void testDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("name", JSType.STRING, new Node());

        assertNull(builder.addProperty("name", JSType.NUMBER, new Node()));
    }

    @Test
    public void testEmptyBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        assertNotNull(builder.build());
    }

    @Test
    public void testNonEmptyBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("name", JSType.STRING, new Node());

        assertNotNull(builder.build());
    }

}