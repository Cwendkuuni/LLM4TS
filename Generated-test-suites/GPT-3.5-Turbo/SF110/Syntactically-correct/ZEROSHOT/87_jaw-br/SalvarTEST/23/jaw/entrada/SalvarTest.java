import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {
    
    @Test
    public void testSalvarConstructor() {
        // create dummy ProcessarEntidades object
        ProcessarEntidades form = new ProcessarEntidades();
        
        // test constructor with SALVAR_XML type
        Salvar salvar1 = new Salvar(form, 10);
        assertNotNull(salvar1);
        
        // test constructor with SALVAR type
        Salvar salvar2 = new Salvar(form, 20);
        assertNotNull(salvar2);
        
        // test constructor with SALVAR_COMO type
        Salvar salvar3 = new Salvar(form, 30);
        assertNotNull(salvar3);
    }
    
    @Test
    public void testSalvarConstructorException() {
        // create dummy ProcessarEntidades object with error
        ProcessarEntidades form = new ProcessarEntidades() {
            @Override
            public List<Entidade> getEntidades() throws Exception {
                throw new Exception("Test exception");
            }
        };
        
        // test constructor with SALVAR_XML type and exception
        Salvar salvar1 = new Salvar(form, 10);
        assertEquals("", salvar1.xml);
        
        // test constructor with SALVAR type and exception
        Salvar salvar2 = new Salvar(form, 20);
        assertEquals("", salvar2.xml);
        
        // test constructor with SALVAR_COMO type and exception
        Salvar salvar3 = new Salvar(form, 30);
        assertEquals("", salvar3.xml);
    }
    
    @Test
    public void testSalvarFileFilter() {
        // create dummy ProcessarEntidades object
        ProcessarEntidades form = new ProcessarEntidades();
        
        // test constructor with SALVAR_XML type
        Salvar salvar1 = new Salvar(form, 10);
        String expectedFilter1 = "XML JAW files";
        String actualFilter1 = salvar1.new Filtro2().getDescription();
        assertEquals(expectedFilter1, actualFilter1);
        
        // test constructor with SALVAR type or SALVAR_COMO type
        Salvar salvar2 = new Salvar(form, 20);
        Salvar salvar3 = new Salvar(form, 30);
        String expectedFilter2 = "JAW files";
        String actualFilter2 = salvar2.new Filtro().getDescription();
        assertEquals(expectedFilter2, actualFilter2);
        String actualFilter3 = salvar3.new Filtro().getDescription();
        assertEquals(expectedFilter2, actualFilter3);
    }
    
    @Test
    public void testSalvarArquivo() {
        // create dummy ProcessarEntidades object
        ProcessarEntidades form = new ProcessarEntidades();
        
        // test constructor with SALVAR_XML type
        Salvar salvar1 = new Salvar(form, 10);
        assertEquals(".jaw.xml", salvar1.arquivo.substring(salvar1.arquivo.length() - 8));
        
        // test constructor with SALVAR type or SALVAR_COMO type
        Salvar salvar2 = new Salvar(form, 20);
        Salvar salvar3 = new Salvar(form, 30);
        
        // check if the file name ends with ".jaw"
        assertTrue(salvar2.arquivo.endsWith(".jaw"));
        assertTrue(salvar3.arquivo.endsWith(".jaw"));
    }
}