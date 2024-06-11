import static org.junit.Assert.*;
import org.junit.*;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public class CrossModuleMethodMotionTest {

  private static final Logger logger =
    Logger.getLogger(CrossModuleMethodMotion.class.getName());
  private CrossModuleMethodMotion crossModuleMethodMotion;

  @Before
  public void setUp() {
    crossModuleMethodMotion = new CrossModuleMethodMotion();
  }

  @Test
  public void testProcess() {
    Node externRoot = new Node();
    Node root = new Node();
    crossModuleMethodMotion.process(externRoot, root);
  }

  @Test
  public void testMoveMethods() {
    Collection<NameInfo> allNameInfo = new Collection<>();
    crossModuleMethodMotion.moveMethods(allNameInfo);
  }

  @Test
  public void testIdGenerator_hasGeneratedAnyIds() {
    IdGenerator idGenerator = new IdGenerator();
    boolean result = idGenerator.hasGeneratedAnyIds();
    assertTrue(result);
  }

  @Test
  public void testIdGenerator_newId() {
    IdGenerator idGenerator = new IdGenerator();
    int result = idGenerator.newId();
    assertEquals(result, 1);
  }
}