import jaw.entrada.Salvar;
import jaw.metamodelo.*;
import jaw.gui.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {
    
    private ProcessarEntidades form;
    private Salvar salvarXml;
    private Salvar salvar;
    private Salvar salvarComo;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvarXml = new Salvar(form, 10);
        salvar = new Salvar(form, 20);
        salvarComo = new Salvar(form, 30);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(salvarXml);
        assertNotNull(salvar);
        assertNotNull(salvarComo);
    }
    
    @Test
    public void testSalvarXML() {
        assertEquals(".jaw.xml", salvarXml.arquivo.substring(salvarXml.arquivo.length() - 8));
        assertEquals(30, salvarXml.tipoSalvar);
        assertNotNull(salvarXml.serializaXML);
        assertNotNull(salvarXml.xml);
    }
    
    @Test
    public void testSalvar() {
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
        assertEquals(20, salvar.tipoSalvar);
        assertNotNull(salvar.salvar);
        assertNotNull(salvar.nomeArquivoSalvo);
        assertNotNull(salvar.xml);
    }
    
    @Test
    public void testSalvarComo() {
        assertEquals(30, salvarComo.tipoSalvar);
        assertNotNull(salvarComo.nomeArquivoSalvo);
    }
}