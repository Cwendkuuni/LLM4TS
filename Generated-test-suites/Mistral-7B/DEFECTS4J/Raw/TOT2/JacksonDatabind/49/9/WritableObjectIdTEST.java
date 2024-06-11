import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderImpl;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }
}