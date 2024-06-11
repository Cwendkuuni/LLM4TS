
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {
    
    private VassalRebellion vassalRebellion;
    
    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }
    
    @Test
    public void testPerform() {
        Collection col = new ArrayList();
        col.add("FiefdomName");
        vassalRebellion.perform(col);
        // Assert statements
    }
}
