import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        geoQuery = new GeoQuery(null);
    }

    @Test
    public void getLocation_NullLocation_ReturnsNull() {
        assertNull(geoQuery.getLocation());
    }

    @Test
    public void getIp_NullIp_ReturnsNull() {
        assertNull(geoQuery.getIp());
    }

    @Test
    public void getAccuracy_DefaultValue_ReturnsNull() {
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void setAccuracy_ValidAccuracy_SetsAccuracy() {
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void getGranularity_DefaultValue_ReturnsNull() {
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void setGranularity_ValidGranularity_SetsGranularity() {
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void getMaxResults_DefaultValue_ReturnsZero() {
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void setMaxResults_ValidMaxResults_SetsMaxResults() {
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    // Add more tests to cover all methods
    
}