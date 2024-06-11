package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {
    @Test
    public void testSalvar() {
        // Test case 1
        ProcessarEntidades form1 = new ProcessarEntidades();
        int tipoSalvar1 = 10;
        Salvar salvar1 = new Salvar(form1, tipoSalvar1);
        
        // Test case 2
        ProcessarEntidades form2 = new ProcessarEntidades();
        int tipoSalvar2 = 20;
        Salvar salvar2 = new Salvar(form2, tipoSalvar2);
        
        // Test case 3
        ProcessarEntidades form3 = new ProcessarEntidades();
        int tipoSalvar3 = 30;
        Salvar salvar3 = new Salvar(form3, tipoSalvar3);
    }
    
    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        
        // Test case 1
        File file1 = new File("test.jaw.xml");
        assertTrue(filtro2.accept(file1));
        
        // Test case 2
        File file2 = new File("test.txt");
        assertFalse(filtro2.accept(file2));
    }
    
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        // Test case 1
        File file1 = new File("test.jaw");
        assertTrue(filtro.accept(file1));
        
        // Test case 2
        File file2 = new File("test.txt");
        assertFalse(filtro.accept(file2));
    }
}