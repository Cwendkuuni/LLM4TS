import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import com.google.common.cache.CacheBuilderSpec;

@RunWith(MockitoJUnitRunner.class)
public class CacheBuilderSpecTest {

    @InjectMocks
    private CacheBuilderSpec cacheBuilderSpec;

    @Test
    public void testParseWithEmptySpec() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec result = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(result);
        assertEquals("", result.toParsableString());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec result = CacheBuilderSpec.disableCaching();
        assertNotNull(result);
        assertTrue(result.toParsableString().contains("maximumSize=0"));
    }

    // Add more test cases for other public methods

}