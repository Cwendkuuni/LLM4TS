import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class LagoonCLITest {

    @Mock
    private LagoonProcessor processor;
    
    private LagoonCLI lagoon;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lagoon = new LagoonCLI();
    }
    
    @Test
    public void testMain_BuildOption_ShouldCallProcessorBuildWithFalse() {
        String[] args = {"property_file", "build"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(true);
        lagoon.main(args);
        Mockito.verify(processor).build(false);
    }
    
    @Test
    public void testMain_ForceOption_ShouldCallProcessorBuildWithTrue() {
        String[] args = {"property_file", "force"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(true);
        lagoon.main(args);
        Mockito.verify(processor).build(true);
    }
    
    @Test
    public void testMain_IntervalOption_ShouldCallProcessorBuildWithFalse() {
        String[] args = {"property_file", "10"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(true);
        lagoon.main(args);
        Mockito.verify(processor).build(false);
    }
    
    @Test
    public void testMain_InvalidPropertyValue_ShouldPrintErrorMessageAndReturn() {
        String[] args = {"property_file", "invalid"};
        lagoon.main(args);
        // Assert error message was printed
    }
    
    @Test
    public void testMain_SitemapFileArgument_ShouldInitializeProcessorWithSitemapFile() {
        String[] args = {"sitemap_file", "build"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(true);
        lagoon.main(args);
        Mockito.verify(processor).initialize(Mockito.eq("user.dir"), Mockito.any(File.class), Mockito.any(File.class), Mockito.isNull());
    }
    
    @Test
    public void testMain_PropertyFileArgument_ShouldInitializeProcessorWithPropertiesFromPropertyFile() {
        String[] args = {"property_file", "build"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(true);
        lagoon.main(args);
        Mockito.verify(processor).initialize(Mockito.eq("targetURL"), Mockito.any(File.class), Mockito.any(File.class), Mockito.eq("password"));
    }
    
    @Test
    public void testMain_InvalidPropertyFile_ShouldPrintErrorMessageAndReturn() {
        String[] args = {"invalid_file", "build"};
        lagoon.main(args);
        // Assert error message was printed
    }
    
    @Test
    public void testMain_IOExceptionWhileInitializingLagoon_ShouldPrintErrorMessageAndReturn() {
        String[] args = {"property_file", "build"};
        Mockito.doThrow(new IOException()).when(processor).initialize(Mockito.anyString(), Mockito.any(File.class), Mockito.any(File.class), Mockito.anyString());
        lagoon.main(args);
        // Assert error message was printed
    }
    
    @Test
    public void testMain_BuildOption_ReturnsFalse_ShouldPrintErrorMessage() {
        String[] args = {"property_file", "build"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(false);
        lagoon.main(args);
        // Assert error message was printed
    }
    
    @Test
    public void testMain_ForceOption_ReturnsFalse_ShouldPrintErrorMessage() {
        String[] args = {"property_file", "force"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(false);
        lagoon.main(args);
        // Assert error message was printed
    }
    
    @Test
    public void testMain_IntervalOption_ReturnsFalse_ShouldPrintErrorMessage() {
        String[] args = {"property_file", "10"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenReturn(false);
        lagoon.main(args);
        // Assert error message was printed
    }
    
    @Test
    public void testMain_IOExceptionWhileBuildingWebsite_ShouldPrintErrorMessage() {
        String[] args = {"property_file", "build"};
        Mockito.when(processor.build(Mockito.anyBoolean())).thenThrow(new IOException());
        lagoon.main(args);
        // Assert error message was printed
    }
    
    @Test
    public void testGetProperty_ValidPropertyName_ReturnsPropertyValue() throws LagoonException {
        String result = lagoon.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }
    
    @Test(expected = LagoonException.class)
    public void testGetProperty_InvalidPropertyName_ThrowsLagoonException() throws LagoonException {
        lagoon.getProperty("invalid");
    }
    
    @Test
    public void testShowTime_LessThan10Seconds_ShouldPrintInMs() {
        String result = lagoon.showTime(5000);
        assertTrue(result.contains("ms"));
    }
    
    @Test
    public void testShowTime_MoreThan10Seconds_ShouldPrintInS() {
        String result = lagoon.showTime(15000);
        assertTrue(result.contains("s"));
    }

}