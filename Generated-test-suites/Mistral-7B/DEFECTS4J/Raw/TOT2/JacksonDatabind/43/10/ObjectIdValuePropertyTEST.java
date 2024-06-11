import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata mockMetadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, mockMetadata);
        assertNotNull(objectIdValueProperty);
    }
}