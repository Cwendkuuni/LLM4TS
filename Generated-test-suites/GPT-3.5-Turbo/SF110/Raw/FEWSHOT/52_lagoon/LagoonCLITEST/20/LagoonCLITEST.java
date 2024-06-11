
package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.core.Authenticate;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.util.AuthCredentials;
import nu.staldal.xtree.Element;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private LagoonCLI lagoonCLI;
    
    @Before
    public void setUp() {
        lagoonCLI = new LagoonCLI();
    }
    
    @Test
    public void testMainNoArgs() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream oldSystemOut = System.out;
        System.setOut(new PrintStream(baos));
        
        LagoonCLI.main(new String[0]);
        
        String expected = "Syntax:\r\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\r\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expected, baos.toString().trim());
        
        System.setOut(oldSystemOut);
    }
    
    // Continue with more test cases here
}
