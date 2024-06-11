import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;
import java.util.List;
import java.util.Vector;

public class SalvarTest {
  
  @Test
  public void testSalvarConstructor() {
    ProcessarEntidades form = mock(ProcessarEntidades.class);
    int tipoSalvar = 10;
    Salvar salvar = new Salvar(form, tipoSalvar);
    
    assertNotNull(salvar);
    assertEquals(false, salvar.vela);
    assertEquals("", salvar.arquivo);
    assertEquals("", salvar.xml);
    assertNotNull(salvar.entidade);
    assertEquals(new Vector<Entidade>(), salvar.entidade);
  }
  
  @Test
  public void testSalvarFiltro2Accept() {
    File file = mock(File.class);
    FileFilter filtro = new Salvar.Filtro2();
    
    when(file.isDirectory()).thenReturn(true);
    assertTrue(filtro.accept(file));

    when(file.isDirectory()).thenReturn(false);
    when(file.getName()).thenReturn("file.jaw.xml");
    assertTrue(filtro.accept(file));
    
    when(file.getName()).thenReturn("file.xml");
    assertFalse(filtro.accept(file));
  }
  
  @Test
  public void testSalvarFiltro2GetDescription() {
    FileFilter filtro = new Salvar.Filtro2();
    assertEquals("XML JAW files", filtro.getDescription());
  }
  
  @Test
  public void testSalvarFiltroAccept() {
    File file = mock(File.class);
    FileFilter filtro = new Salvar.Filtro();
    
    when(file.isDirectory()).thenReturn(true);
    assertTrue(filtro.accept(file));
    
    when(file.isDirectory()).thenReturn(false);
    when(file.getName()).thenReturn("file.jaw");
    assertTrue(filtro.accept(file));
    
    when(file.getName()).thenReturn("file.xml");
    assertFalse(filtro.accept(file));
  }
  
  @Test
  public void testSalvarFiltroGetDescription() {
    FileFilter filtro = new Salvar.Filtro();
    assertEquals("JAW files", filtro.getDescription());
  }
  
  // Add more test methods for each public method in the Salvar class
  
}