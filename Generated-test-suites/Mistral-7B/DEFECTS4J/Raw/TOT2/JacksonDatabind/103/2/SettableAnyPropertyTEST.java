import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    @Test
    public void testGetProperty() {
        BeanProperty property = new BeanProperty(new AnnotatedField(TestClass.class, "testProperty"));
        AnnotatedMember setter = new AnnotatedField(TestClass.class, "testProperty");
        JavaType type = new TypeFactory().constructType(TestClass.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);
        assertEquals(property, settableAnyProperty.getProperty());
    }
}

class TestClass {
    // Add a test property here
}