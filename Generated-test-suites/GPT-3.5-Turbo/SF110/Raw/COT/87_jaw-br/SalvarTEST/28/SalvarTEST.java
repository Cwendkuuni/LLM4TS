import org.junit.*;
import static org.junit.Assert.*;

public class SalvarTest {
  
  @Test
  public void testSalvarConstructor() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR_XML;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertNotNull(salvar);
  }
  
  @Test
  public void testSalvarConstructor_withException() {
    ProcessarEntidades form = null;
    int tipoSalvar = Salvar.SALVAR_XML;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertNull(salvar);
  }
  
  @Test
  public void testSalvarSalvarXML() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR_XML;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertNotNull(salvar.xml);
  }
  
  @Test
  public void testSalvarSalvar() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertNotNull(salvar.arquivo);
  }
  
  @Test
  public void testSalvarSalvarComo() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR_COMO;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertNotNull(salvar.arquivo);
  }
  
  @Test
  public void testSalvarSalvarComo_withNovoProjeto() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR;
    form.setNomeArquivoSalvo("Novo Projeto");
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertNotNull(salvar.arquivo);
  }
  
  @Test
  public void testSalvarSalvarComo_withInvalidExtension() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR;
    form.setNomeArquivoSalvo("test");
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.lastIndexOf(".")));
  }
  
  @Test
  public void testSalvarSalvar_success() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR;
    form.setNomeArquivoSalvo("test.jaw");
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals("Arquivo salvo com sucesso!", JOptionPaneUMsg);
  }
  
  @Test
  public void testSalvarSalvar_withException() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR;
    form.setNomeArquivoSalvo("test.jaw");
    salvar.setArquivo("test");
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals("N\u00c3£o foi poss\u00c3\u00advel salvar esse arquivo\n Verifique o formato das entidades !", JOptionPaneUMsg);
  }
  
  @Test
  public void testSalvarSalvarComoXML() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR_COMO;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals(".jaw.xml", salvar.arquivo.substring(salvar.arquivo.lastIndexOf(".")));
  }
  
  @Test
  public void testSalvarSalvarComoXML_success() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR_COMO;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals("Arquivo Exportado com sucesso!", JOptionPaneUMsg);
  }
  
  @Test
  public void testFiltro2AcceptJAWXML() {
    Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
    File file = new File("test.jaw.xml");
    assertTrue(filtro.accept(file));
  }
  
  @Test
  public void testFiltro2AcceptInvalidExtension() {
    Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
    File file = new File("test.invalid");
    assertFalse(filtro.accept(file));
  }
  
  @Test
  public void testFiltro2Description() {
    Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
    assertEquals("XML JAW files", filtro.getDescription());
  }
  
  @Test
  public void testFiltroAcceptJAW() {
    Salvar.Filtro filtro = new Salvar().new Filtro();
    File file = new File("test.jaw");
    assertTrue(filtro.accept(file));
  }
  
  @Test
  public void testFiltroAcceptInvalidExtension() {
    Salvar.Filtro filtro = new Salvar().new Filtro();
    File file = new File("test.invalid");
    assertFalse(filtro.accept(file));
  }
  
  @Test
  public void testFiltroDescription() {
    Salvar.Filtro filtro = new Salvar().new Filtro();
    assertEquals("JAW files", filtro.getDescription());
  }
}