
import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {
  
  @Test
  public void testSalvarConstructor() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = 10;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals(10, salvar.SALVAR_XML);
    assertNull(salvar.form);
  }
  
  @Test
  public void testSalvarConstructor_ArquivoSalvo() {
    ProcessarEntidades form = new ProcessarEntidades();
    form.setNomeArquivoSalvo("Arquivo1");
    int tipoSalvar = 20;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals(20, salvar.SALVAR);
    assertEquals("Arquivo1", salvar.arquivo);
  }
  
  @Test
  public void testSalvarConstructor_EmptyEntidades() {
    ProcessarEntidades form = new ProcessarEntidades();
    form.setNomeArquivoSalvo("Arquivo2");
    int tipoSalvar = 30;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertEquals(30, salvar.SALVAR_COMO);
    assertTrue(salvar.entidade.isEmpty());
  }
  
  @Test
  public void testSalvarConstructor_Exception() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = 10;
    Salvar salvar = new Salvar(form, tipoSalvar);
    assertNull(salvar.xml);
    assertNull(salvar.entidade);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes_Case1() {
    Salvar salvar = new Salvar();
    String str = "\"foo\"";
    assertEquals("foo", Salvar.stripLeadingAndTrailingQuotes(str));
    assertEquals("foo ", Salvar.stripLeadingAndTrailingQuotes("foo \"bar\""));
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes_Case2() {
    Salvar salvar = new Salvar();
    String str = "\"foo\" bar";
    assertEquals("foo\" bar", Salvar.stripLeadingAndTrailingQuotes(str));
    assertEquals("\"foo\" and \"bar\"", Salvar.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes_Case3() {
    Salvar salvar = new Salvar();
    String str = "";
    assertEquals("", Salvar.stripLeadingAndTrailingQuotes(str));
  }
  
  @Test
  public void testStripLeadingHyphens_Case1() {
    Salvar salvar = new Salvar();
    String str = "-f";
    assertEquals("f", Salvar.stripLeadingHyphens(str));
  }
  
  @Test
  public void testStripLeadingHyphens_Case2() {
    Salvar salvar = new Salvar();
    String str = "--foo";
    assertEquals("foo", Salvar.stripLeadingHyphens(str));
  }
  
  @Test
  public void testStripLeadingHyphens_Case3() {
    Salvar salvar = new Salvar();
    String str = "---foo";
    assertEquals("-foo", Salvar.stripLeadingHyphens(str));
  }
  
  @Test
  public void testStripLeadingHyphens_Null() {
    Salvar salvar = new Salvar();
    assertNull(Salvar.stripLeadingHyphens(null));
  }
}
