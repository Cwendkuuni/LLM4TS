import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SalvarTest {
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertEquals("foo", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(10, salvar.SALVAR_XML);
        
        form.setNomeArquivoSalvo("Novo Projeto");
        tipoSalvar = 20;
        Salvar salvar2 = new Salvar(form, tipoSalvar);

        assertNotNull(salvar2.arquivo);
        assertEquals(".jaw", salvar2.arquivo);
        assertNotNull(salvar2.entidade);
        assertEquals(20, salvar2.SALVAR);
        
        form.setNomeArquivoSalvo("");
        tipoSalvar = 30;
        Salvar salvar3 = new Salvar(form, tipoSalvar);

        assertNotNull(salvar3.arquivo);
        assertEquals(".jaw", salvar3.arquivo);
        assertNotNull(salvar3.entidade);
        assertEquals(30, salvar3.SALVAR_COMO);
    }

    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        
        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.xml")));
        assertFalse(filtro2.accept(new File("test.jaw")));
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.xml")));
        assertFalse(filtro.accept(new File("test.jaw.xml")));
        
        assertEquals("JAW files", filtro.getDescription());
    }
}