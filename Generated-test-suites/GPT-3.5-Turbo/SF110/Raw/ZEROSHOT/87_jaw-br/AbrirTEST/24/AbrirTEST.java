import org.junit.*;
import static org.junit.Assert.*;

public class AbrirTest {

  private ProcessarEntidades form;
  private Abrir abrir;

  @Before
  public void setUp() {
    form = new ProcessarEntidades();
    abrir = new Abrir(form, "arquivo");
  }

  @Test
  public void testAbrirXML() {
    abrir.abrirXML("arquivo.jaw.xml");
    // Add assertions
  }

  @Test
  public void testAbrirSerializado() {
    abrir.abrirSerializado("arquivo.jaw");
    // Add assertions
  }

  @After
  public void tearDown() {
    form = null;
    abrir = null;
  }
}