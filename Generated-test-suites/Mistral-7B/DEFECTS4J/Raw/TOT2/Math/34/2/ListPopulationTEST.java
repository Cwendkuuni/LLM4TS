import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
        // Add some chromosomes to test the population limit
        population.addChromosome(new Chromosome() {});
        population.addChromosome(new Chromosome() {});
        assertEquals(5, population.getPopulationSize());
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(new Chromosome() {}));
    }

    @Test
    public void testListPopulationConstructorWithEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationSize());
        // Add some chromosomes to test the population limit
        population.addChromosome(new Chromosome() {});
        population.addChromosome(new Chromosome() {});
        assertEquals(5, population.getPopulationSize());
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(new Chromosome() {}));
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(List.of(new Chromosome() {}, new Chromosome() {}));
        assertEquals(5, population.getPopulationSize());
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(List.of(new Chromosome() {})));
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {});
        population.addChromosome(new Chromosome() {});
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(5, chromosomes.size());
        // Test that the list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> chromosomes.remove(0));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
        Chromosome fittest = population.getFittestChromosome();
        assertEquals(population.getChromosomes().get(1), fittest);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
    }
}