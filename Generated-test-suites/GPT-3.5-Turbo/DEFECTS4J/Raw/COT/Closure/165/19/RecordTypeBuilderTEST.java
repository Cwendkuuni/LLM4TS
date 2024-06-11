import junit.framework.TestCase;
public class RecordTypeBuilderTest extends TestCase {

    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType type1 = new JSType();
        Node propertyNode1 = new Node();
        assertEquals(builder.addProperty("name1", type1, propertyNode1), builder);

        JSType type2 = new JSType();
        Node propertyNode2 = new Node();
        assertEquals(builder.addProperty("name2", type2, propertyNode2), builder);
    }

    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType type1 = new JSType();
        Node propertyNode1 = new Node();
        builder.addProperty("name1", type1, propertyNode1);

        JSType type2 = new JSType();
        Node propertyNode2 = new Node();
        builder.addProperty("name2", type2, propertyNode2);

        JSType recordType = builder.build();
        assertNotNull(recordType);
    }
}