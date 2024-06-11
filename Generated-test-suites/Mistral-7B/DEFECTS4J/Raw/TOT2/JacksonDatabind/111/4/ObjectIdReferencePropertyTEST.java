import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdReferencePropertyTest {
    private ObjectIdReferenceProperty objIdReferenceProperty;
    private ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonType.STD_CLASS, JsonType.STD_CLASS);

    @Before
    public void setup() {
        objIdReferenceProperty = new ObjectIdReferenceProperty(new SettableBeanProperty(null, null), objectIdInfo);
    }

    @Test
    public void testConstructorWithSettableBeanPropertyAndObjectIdInfo() {
        assertNotNull(objIdReferenceProperty);
        assertEquals(ObjectIdReferenceProperty.class, objIdReferenceProperty.getClass());
        assertEquals(objectIdInfo, objIdReferenceProperty._objectIdInfo);
        assertNull(objIdReferenceProperty._forward);
    }
}