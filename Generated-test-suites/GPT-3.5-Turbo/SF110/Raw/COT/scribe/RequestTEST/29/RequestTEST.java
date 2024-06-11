
import org.junit.*;
import org.scribe.model.*;
import org.scribe.exceptions.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class RequestTest {
    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://localhost");
    }

    // ... (All other test methods)

}