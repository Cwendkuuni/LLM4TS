import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import jaw.entrada.Filtro;
import jaw.entrada.Filtro2;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.ProcessarEntidades;
import jawosto.Main;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades formMock;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @Before
    public void setUp() {
        formMock = mock(ProcessarEntidades.class);
        salvar = new Salvar(formMock, 20);
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
  
    @Test
    public void testConstructor() {
        assertNotNull(salvar);
    }
  
    @Test
    public void testSalvarXml() {
        Vector<Entidade> entidades = new Vector<>();
        Entidade entidadeMock = mock(Entidade.class);
        entidades.add(entidadeMock);
        when(formMock.getEntidades()).thenReturn(entidades);
        salvar = new Salvar(formMock, 10);
        String expectedXml = "Mocked XML";
        assertEquals(expectedXml, salvar.xml);
        assertEquals("Arquivo Exportado com sucesso!", outContent.toString());
    }
  
    @Test
    public void testSalvar() {
        Vector<Entidade> entidades = new Vector<>();
        Entidade entidadeMock = mock(Entidade.class);
        entidades.add(entidadeMock);
        when(formMock.getEntidades()).thenReturn(entidades);
        salvar = new Salvar(formMock, 20);
        assertEquals("Arquivo salvo com sucesso!", outContent.toString());
    }
  
    @Test
    public void testVel() {
        Vector<Entidade> entidades = new Vector<>();
        Entidade entidadeMock = mock(Entidade.class);
        entidades.add(entidadeMock);
        when(formMock.getEntidades()).thenReturn(entidades);
        salvar = new Salvar(formMock, 30);
        assertFalse(salvar.vela);
    }
  
    @Test
    public void testVelaTrue() {
        Vector<Entidade> entidades = new Vector<>();
        Entidade entidadeMock = mock(Entidade.class);
        entidades.add(entidadeMock);
        when(formMock.getEntidades()).thenReturn(entidades);
        salvar = new Salvar(formMock, 10);
        assertTrue(salvar.vela);
    }
  
    @Test
    public void testVelaFalse() {
        Vector<Entidade> entidades = new Vector<>();
        Entidade entidadeMock = mock(Entidade.class);
        entidades.add(entidadeMock);
        when(formMock.getEntidades()).thenReturn(entidades);
        salvar = new Salvar(formMock, 10);
        assertTrue(salvar.vela);
        when(formMock.getName()).thenReturn("");
        salvar = new Salvar(formMock, 10);
        assertEquals("", salvar.arquivo);
    }
  
    @Test
    public void testArquivoEndsWith() {
        Vector<Entidade> entidades = new Vector<>();
        Entidade entidadeMock = mock(Entidade.class);
        entidades.add(entidadeMock);
        when(formMock.getEntidades()).thenReturn(entidades);
        when(formMock.getNomeArquivoSalvo()).thenReturn("Novo Projeto");
        salvar = new Salvar(formMock, 20);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
    }

    @Test
    public void testFiltro2() {
        Filtro2 filtro = new Salvar().new Filtro2();
        assertTrue(filtro.accept(new File("testFile.jaw.xml")));
        assertTrue(filtro.accept(new File("testFile.JAW.XML")));
        assertFalse(filtro.accept(new File("testFile.jawxml")));
        assertFalse(filtro.accept(new File("testFile.jaw")));
        assertFalse(filtro.accept(new File("testFile")));
        assertEquals("XML JAW files", filtro.getDescription());
    }
  
    @Test
    public void testFiltro() {
        Filtro filtro = new Salvar().new Filtro();
        assertTrue(filtro.accept(new File("testFile.jaw")));
        assertTrue(filtro.accept(new File("testFile.JAW")));
        assertFalse(filtro.accept(new File("testFile.jawxml")));
        assertFalse(filtro.accept(new File("testFile.jaw.xml")));
        assertFalse(filtro.accept(new File("testFile")));
        assertEquals("JAW files", filtro.getDescription());
    }

}