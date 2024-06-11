
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("opt", "description");
    }

    @Test
    public void testGetOpt() {
        assertEquals("opt", option.getOpt());
    }

    // ...rest of the test methods...

}