import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LagoonCLITest {

    private LagoonCLI lagoonCLI;
    private LagoonProcessor lagoonProcessorMock;

    @Before
    public void setUp() throws Exception {
        lagoonCLI = new LagoonCLI();
        lagoonProcessorMock = mock(LagoonProcessor.class);
    }

    @After
    public void tearDown() throws Exception {
        lagoonCLI = null;
        lagoonProcessorMock = null;
    }
  
    @Test
    public void testMain_WhenPropertyFileProvidedAndBuildFlagSetToTrue_ThenProcessorBuildCalledWithFalse() {
        // Arrange
        String[] args = {"property_file", "build"};
        when(lagoonProcessorMock.build(false)).thenReturn(true);
        LagoonCLI mainClassSpy = spy(lagoonCLI);
        doReturn(lagoonProcessorMock).when(mainClassSpy).createLagoonProcessor(any(), any(), any(), any(), any(), any());
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        mainClassSpy.main(args);

        // Assertion
        verify(lagoonProcessorMock, times(1)).build(false);
        verify(outMock).println("Building website...");
        verify(outMock).println("...with errors...");
    }

    @Test
    public void testMain_WhenPropertyFileProvidedAndForceFlagSetToTrue_ThenProcessorBuildCalledWithTrue() {
        // Arrange
        String[] args = {"property_file", "force"};
        when(lagoonProcessorMock.build(true)).thenReturn(true);
        LagoonCLI mainClassSpy = spy(lagoonCLI);
        doReturn(lagoonProcessorMock).when(mainClassSpy).createLagoonProcessor(any(), any(), any(), any(), any(), any());
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        mainClassSpy.main(args);

        // Assertion
        verify(lagoonProcessorMock, times(1)).build(true);
        verify(outMock).println("Force building website...");
        verify(outMock).println("...with errors...");
    }

    @Test
    public void testMain_WhenPropertyFileProvidedAndIntegerIntervalProvided_ThenProcessorBuildCalledInLoop() throws InterruptedException {
        // Arrange
        String[] args = {"property_file", "5"};
        when(lagoonProcessorMock.build(false)).thenReturn(true).thenReturn(true).thenThrow(new RuntimeException());
        LagoonCLI mainClassSpy = spy(lagoonCLI);
        doReturn(lagoonProcessorMock).when(mainClassSpy).createLagoonProcessor(any(), any(), any(), any(), any(), any());
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        mainClassSpy.main(args);
        Thread.sleep(6000);

        // Assertion
        verify(lagoonProcessorMock, times(2)).build(false);
        verify(outMock, times(2)).println("Building website...");
        verify(outMock, times(2)).println("...with errors...");
    }

    @Test
    public void testMain_WhenPropertyFileNotProvided_ThenErrorMessagePrinted() {
        // Arrange
        String[] args = {};
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        lagoonCLI.main(args);

        // Assertion
        verify(outMock).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test
    public void testMain_WhenPropertyFileProvidedAndInvalidBuildFlag_ThenErrorMessagePrinted() {
        // Arrange
        String[] args = {"property_file", "invalidFlag"};
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        lagoonCLI.main(args);

        // Assertion
        verify(outMock).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test
    public void testMain_WhenPropertyFileProvidedAndNegativeIntervalProvided_ThenErrorMessagePrinted() {
        // Arrange
        String[] args = {"property_file", "-5"};
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        lagoonCLI.main(args);

        // Assertion
        verify(outMock).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test
    public void testMain_WhenSitemapFileProvidedAndBuildFlagSetToTrue_ThenProcessorBuildCalledWithFalse() {
        // Arrange
        String[] args = {"sitemap_file", "build"};
        when(lagoonProcessorMock.build(false)).thenReturn(true);
        LagoonCLI mainClassSpy = spy(lagoonCLI);
        doReturn(lagoonProcessorMock).when(mainClassSpy).createLagoonProcessor(any(), any(), any(), any(), any(), any());
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        mainClassSpy.main(args);

        // Assertion
        verify(lagoonProcessorMock, times(1)).build(false);
        verify(outMock).println("Building website...");
        verify(outMock).println("...with errors...");
    }

    @Test
    public void testMain_WhenSitemapFileProvidedAndForceFlagSetToTrue_ThenProcessorBuildCalledWithTrue() {
        // Arrange
        String[] args = {"sitemap_file", "force"};
        when(lagoonProcessorMock.build(true)).thenReturn(true);
        LagoonCLI mainClassSpy = spy(lagoonCLI);
        doReturn(lagoonProcessorMock).when(mainClassSpy).createLagoonProcessor(any(), any(), any(), any(), any(), any());
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        mainClassSpy.main(args);

        // Assertion
        verify(lagoonProcessorMock, times(1)).build(true);
        verify(outMock).println("Force building website...");
        verify(outMock).println("...with errors...");
    }

    @Test
    public void testMain_WhenSitemapFileProvidedAndIntegerIntervalProvided_ThenProcessorBuildCalledInLoop() throws InterruptedException {
        // Arrange
        String[] args = {"sitemap_file", "5"};
        when(lagoonProcessorMock.build(false)).thenReturn(true).thenReturn(true).thenThrow(new RuntimeException());
        LagoonCLI mainClassSpy = spy(lagoonCLI);
        doReturn(lagoonProcessorMock).when(mainClassSpy).createLagoonProcessor(any(), any(), any(), any(), any(), any());
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        mainClassSpy.main(args);
        Thread.sleep(6000);

        // Assertion
        verify(lagoonProcessorMock, times(2)).build(false);
        verify(outMock, times(2)).println("Building website...");
        verify(outMock, times(2)).println("...with errors...");
    }

    @Test
    public void testMain_WhenSitemapFileNotProvided_ThenErrorMessagePrinted() {
        // Arrange
        String[] args = {};
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        lagoonCLI.main(args);

        // Assertion
        verify(outMock).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test
    public void testMain_WhenSitemapFileProvidedAndInvalidBuildFlag_ThenErrorMessagePrinted() {
        // Arrange
        String[] args = {"sitemap_file", "invalidFlag"};
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        lagoonCLI.main(args);

        // Assertion
        verify(outMock).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test
    public void testMain_WhenSitemapFileProvidedAndNegativeIntervalProvided_ThenErrorMessagePrinted() {
        // Arrange
        String[] args = {"sitemap_file", "-5"};
        PrintStream outMock = mock(PrintStream.class);
        System.setOut(outMock);

        // Act
        lagoonCLI.main(args);

        // Assertion
        verify(outMock).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    private LagoonProcessor createLagoonProcessor(String targetURL, Element sitemapTree, File sourceDir, String password, PrintWriter printWriter1, PrintWriter printWriter2) throws AuthenticationMissingException, AuthenticationException, IOException, LagoonException {
        return new LagoonProcessor(targetURL, sitemapTree, sourceDir, password, printWriter1, printWriter2);
    }
}