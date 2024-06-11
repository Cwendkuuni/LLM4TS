import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidParameters() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});

        ListPopulation populationWithChromosomes = new ListPopulation(chromosomes, 10);
        assertEquals(10, populationWithChromosomes.getPopulationLimit());
        assertEquals(2, populationWithChromosomes.getPopulationSize());
        assertEquals(chromosomes, populationWithChromosomes.getChromosomes());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome() {});
        }
        new ListPopulation(chromosomes, 10);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});

        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(chromosomes);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome() {});
        }
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        population.addChromosomes(chromosomes);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome() {});
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome fittestChromosome = new Chromosome() {
            @Override
            public double getFitness() {
                return 10.0;
            }
        };
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 5.0;
            }
        });
        chromosomes.add(fittestChromosome);

        ListPopulation population = new ListPopulation(chromosomes);
        assertEquals(fittestChromosome, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome() {});
        }
        ListPopulation population = new ListPopulation(chromosomes);
        assertEquals("[" + chromosomes + "]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome() {});
        }
        ListPopulation population = new ListPopulation(chromosomes);

        Iterator<Chromosome> iterator = population.iterator();
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = iterator.next();
            assertNotNull(chromosome);
        }
        assertFalse(iterator.hasNext());
    }
}