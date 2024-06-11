import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {
    // ...

    @Test
    public void testConstructor_SettableBeanPropertyAndObjectIdInfo() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        // Add assertions here
    }

    // ...
}

