import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {
    
    private Salvar salvar;
    private ProcessarEntidades form;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvar = new Salvar(form, Salvar.SALVAR);
    }
    
    @Test
    public void testSalvartipoSalvar() {
        assertEquals(Salvar.SALVAR, salvar.tipoSalvar);
    }
    
    @Test
    public void testSalvarVela() {
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testSalvarArquivo() {
        assertEquals("", salvar.arquivo);
    }
    
    @Test
    public void testSalvarXML() {
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvarEntidade() {
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarEscolhedor() {
        assertNotNull(salvar.escolhedor);
    }
    
    @Test
    public void testSalvarEntidades() {
        assertNotNull(salvar.entidades);
    }
    
    @Test
    public void testSalvarSerializacao() {
        assertNotNull(salvar.salvar);
    }
    
    @Test
    public void testSalvarSerializacaoXML() {
        assertNotNull(salvar.serializaXML);
    }
    
    @Test
    public void testSalvarFiltro2() {
        assertNotNull(salvar.filtro2);
    }
    
    @Test
    public void testSalvarFiltro() {
        assertNotNull(salvar.filtro);
    }
    
    @Test
    public void testSalvarexception() {
        try {
            form.getEntidades();
        } catch (Exception ex) {
            assertEquals(ex.toString(), Main.janelaPrincipal.log());
        }
    }
    
    // Add more test methods to cover all methods in the Salvar class
    
}