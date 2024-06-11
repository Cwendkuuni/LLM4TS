import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class MappingIteratorTest {

    @Test
    public void testHasNext() {
        MappingIterator<Object> iterator = new MappingIterator<>();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        MappingIterator<Integer> iterator = new MappingIterator<>();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    @Test
    public void testRemove() {
        MappingIterator<String> iterator = new MappingIterator<>();
        assertThrows(UnsupportedOperationException.class, () -> {
            iterator.remove();
        });
    }

    @Test
    public void testClose() throws Exception {
        MappingIterator<Boolean> iterator = new MappingIterator<>();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        iterator._parser = jsonParser;
        iterator.close();
        Mockito.verify(jsonParser).close();
    }

    @Test
    public void testNextValue() {
        MappingIterator<Double> iterator = new MappingIterator<>();
        assertThrows(NoSuchElementException.class, () -> {
            iterator.nextValue();
        });
    }

    @Test
    public void testReadAll() {
        MappingIterator<Integer> iterator = new MappingIterator<>();
        assertThrows(NoSuchElementException.class, () -> {
            iterator.readAll();
        });
    }

    @Test
    public void testGetParser() {
        MappingIterator<Boolean> iterator = new MappingIterator<>();
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        MappingIterator<String> iterator = new MappingIterator<>();
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        MappingIterator<Integer> iterator = new MappingIterator<>();
        assertThrows(NullPointerException.class, () -> {
            iterator.getCurrentLocation();
        });
    }

}