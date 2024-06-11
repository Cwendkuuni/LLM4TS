import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import static org.junit.Assert.*;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setup() {
        listPopulation = new ListPopulation(10);
    }

    @Test
    public void testListPopulation_Constructor_ValidPopulationLimit() {
        assertEquals(0, listPopulation.getChromosomes().size());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulation_Constructor_InvalidPopulationLimit() {
        new ListPopulation(0);
    }

    @Test
    public void testListPopulation_Constructor_NullChromosomes() {
        ListPopulation listPopulation = new ListPopulation(null, 10);
        assertEquals(0, listPopulation.getChromosomes().size());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testListPopulation_Constructor_CorrectChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        listPopulation = new ListPopulation(chromosomes, 10);
        assertEquals(2, listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulation_SetChromosomes_NullChromosomes() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulation_SetChromosomes_InvalidChromosomesSize() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulation_SetChromosomes_ValidChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(2, listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulation_AddChromosomes_ExceedingPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulation_AddChromosomes_ValidChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        listPopulation.addChromosomes(chromosomes);
        assertEquals(2, listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test
    public void testListPopulation_GetChromosomes_UnmodifiableList() {
        List<Chromosome> chromosomes = listPopulation.getChromosomes();
        try {
            chromosomes.add(new Chromosome());
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals(0, listPopulation.getChromosomes().size());
        }
    }

    @Test
    public void testListPopulation_GetChromosomeList_ReturnSameList() {
        List<Chromosome> chromosomes = listPopulation.getChromosomeList();
        Chromosome chromosome = new Chromosome();
        chromosomes.add(chromosome);
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulation_AddChromosome_ExceedingPopulationLimit() {
        for (int i = 0; i < 10; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testListPopulation_AddChromosome_ValidChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testListPopulation_GetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(20);
        Chromosome chromosome3 = new Chromosome(15);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testListPopulation_GetPopulationLimit() {
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulation_SetPopulationLimit_InvalidPopulationLimit() {
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulation_SetPopulationLimit_InvalidSize() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        listPopulation = new ListPopulation(chromosomes, 10);
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testListPopulation_SetPopulationLimit_ValidPopulationLimit() {
        listPopulation.setPopulationLimit(20);
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    @Test
    public void testListPopulation_GetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());
        Chromosome chromosome1 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulation_Iterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}