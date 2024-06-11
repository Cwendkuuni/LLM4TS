import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class MappingIteratorTest {

    @Mock
    private JsonParser parser;
    
    @Mock
    private JavaType type;

    @Mock
    private StdDeserializer<Object> deserializer;

    @Mock
    private MappingIterator<Object> mappingIterator;

    private List<Object> resultList;
    
    @Before
    public void setup() {
        mappingIterator = new MappingIterator<>(type, parser, null, deserializer, true, null);
        resultList = new ArrayList<>();
    }

    @Test
    public void testHasNext() throws IOException {
        when(parser.getCurrentToken()).thenReturn(null);
        assertFalse(mappingIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextThrowNoSuchElement() throws IOException {
        when(mappingIterator.hasNextValue()).thenReturn(false);
        mappingIterator.next();
    }

    @Test
    public void testNext() throws IOException {
        Object expected = new Object();
        when(mappingIterator.hasNextValue()).thenReturn(true);
        when(deserializer.deserialize(any(), any())).thenReturn(ClassUtil.cast(expected));
        assertEquals(expected, mappingIterator.next());
    }

    @Test
    public void testReadAll() throws IOException {
        Object obj1 = new Object();
        Object obj2 = new Object();
        when(deserializer.deserialize(any(), any())).thenReturn(ClassUtil.cast(obj1), ClassUtil.cast(obj2));
        when(mappingIterator.hasNextValue()).thenReturn(true, true, false);
        mappingIterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(obj1));
        assertTrue(resultList.contains(obj2));
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        verify(parser, times(1)).close();
    }

    @Test(expected = RuntimeException.class)
    public void testNextThrowRuntimeException() throws IOException {
        when(mappingIterator.hasNextValue()).thenThrow(RuntimeException.class);
        mappingIterator.next();
    }

}