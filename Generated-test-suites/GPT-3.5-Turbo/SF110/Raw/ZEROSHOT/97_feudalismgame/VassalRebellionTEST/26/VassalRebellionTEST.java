import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform() {
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom 1");
        vassalRebellion.perform(col);
      
        // add assertions here to test the perform method
    }

}