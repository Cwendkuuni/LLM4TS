import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class MappingIteratorTest {

    @Mock
    private JsonParser mockParser;

    @Test
    public void hasNextValue_WhenParserIsNull_ReturnsFalse() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void nextValue_WhenHasNextValueIsFalse_ThrowsNoSuchElementException() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertThrows(NoSuchElementException.class, iterator::nextValue);
    }

    @Test
    public void readAll_WhenHasNextValueIsFalse_ReturnsEmptyList() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertTrue(iterator.readAll().isEmpty());
    }

    @Test
    public void getParser_ReturnsCorrectParser() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<>(null, mockParser, null, null, false, null);
        assertEquals(mockParser, iterator.getParser());
    }

    @Test
    public void getCurrentLocation_ReturnsCorrectLocation() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<>(null, mockParser, null, null, false, null);
        assertNotNull(iterator.getCurrentLocation());
    }

    // Add more test cases for other methods and edge cases

}