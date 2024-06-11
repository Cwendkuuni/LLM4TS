
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {
  
  private Salvar salvar;
  
  @Before
  public void setUp() {
    salvar = new Salvar(null, 0);
  }
  
  @Test
  public void testSalvar() {
    assertEquals("", salvar.arquivo);
    assertEquals("", salvar.xml);
    assertEquals(new Vector<Entidade>(), salvar.entidade);
    assertEquals(false, salvar.vela);
  }
  
  @Test
  public void testFiltro2Accept() {
    Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
    assertEquals(true, filtro2.accept(new File("test.jaw.xml")));
    assertEquals(true, filtro2.accept(new File("test.JAW.XML")));
    assertEquals(false, filtro2.accept(new File("test.xml")));
    assertEquals(false, filtro2.accept(new File("test.jaw")));
  }
  
  @Test
  public void testFiltro2GetDescription() {
    Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
    assertEquals("XML JAW files", filtro2.getDescription());
  }
  
  @Test
  public void testFiltroAccept() {
    Salvar.Filtro filtro = new Salvar().new Filtro();
    assertEquals(true, filtro.accept(new File("test.jaw")));
    assertEquals(true, filtro.accept(new File("test.JAW")));
    assertEquals(false, filtro.accept(new File("test.xml")));
    assertEquals(false, filtro.accept(new File("test.jaw.xml")));
  }
  
  @Test
  public void testFiltroGetDescription() {
    Salvar.Filtro filtro = new Salvar().new Filtro();
    assertEquals("JAW files", filtro.getDescription());
  }
}
